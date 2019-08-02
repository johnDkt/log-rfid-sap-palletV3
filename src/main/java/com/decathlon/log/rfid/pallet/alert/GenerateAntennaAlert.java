package com.decathlon.log.rfid.pallet.alert;

import com.decathlon.log.rfid.pallet.alert.message.AlertMessages;
import com.decathlon.log.rfid.pallet.alert.service.BoAlert;
import com.decathlon.log.rfid.pallet.alert.service.BoAlertFactory;
import com.decathlon.log.rfid.pallet.utils.DaysUtils;
import com.decathlon.log.rfid.pallet.utils.ReadText;
import com.decathlon.log.rfid.pallet.utils.WriteText;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Allow to generate alert about the receptions of antennas signal
 *
 * @author Simon BILLIAU
 */
public class GenerateAntennaAlert {

    private static final Logger LOGGER = Logger.getLogger(GenerateAntennaAlert.class);
    private Predicate<String> isAntenna1 = new Predicate<String>() {
        @Override
        public boolean apply(String arg0) {
            return arg0 == AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL");
        }
    };
    private Predicate<String> isAntenna2 = new Predicate<String>() {
        @Override
        public boolean apply(String arg0) {
            return arg0 == AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL");
        }
    };
    private Predicate<String> isAntenna3 = new Predicate<String>() {
        @Override
        public boolean apply(String arg0) {
            return arg0 == AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL");
        }
    };
    private Predicate<String> isAntenna4 = new Predicate<String>() {
        @Override
        public boolean apply(String arg0) {
            return arg0 == AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL");
        }
    };

    public static void main(String[] args) throws IOException {

        Properties log = new Properties();
        log.load(ClassLoader.getSystemResourceAsStream("log4j-pallet.properties"));
        PropertyConfigurator.configure(log);

        GenerateAntennaAlert generateAlert = new GenerateAntennaAlert();
        try {
            DateTime lastDay = DateTime.now().minusDays(1);
            generateAlert.lauch(BoAlertFactory.getBoAlert(), lastDay);
        } catch (final EmailException e) {
            LOGGER.error("Error while send mail alert antenna", e);
        } catch (final IOException e) {
            LOGGER.error("Error while lauch generate alert", e);
        } catch (final MessagingException e) {
            LOGGER.error("The mailing configuration has a problem", e);
        }

    }

    public void lauch(BoAlert alert, DateTime lastDay) throws EmailException, IOException, MessagingException {
        if (generate(lastDay)) {
            LOGGER.info("Start generate alert antenna for date : " + lastDay);
            List<String> allAntennasNotReadForLastDay = getAntennasNotReadFromLogs(lastDay);
            writeNotReadAntennasInFile(allAntennasNotReadForLastDay, lastDay);

            List<String> antennasNotReadForFourDays = getAntennasNotReadForFourDays(lastDay);

            if (!antennasNotReadForFourDays.isEmpty()) {
                alert.sendRapportAlertAntenna(lastDay, antennasNotReadForFourDays);
                LOGGER.info("Email sent");
            }

            cleanAntennasFile(lastDay);

        } else {
            LOGGER.info("No check antenna for this date: " + lastDay);
        }

    }

    private void writeNotReadAntennasInFile(List<String> allAntennasNotReadForLastDay, DateTime day) throws IOException {
        WriteText.writeInFile(new File(AlertMessages.getString("RFID.PALLET.LOG.FILE.RESUME")), allAntennasNotReadForLastDay, day);
        LOGGER.info("Wrote " + allAntennasNotReadForLastDay.size() + " antennas in file");
    }

    private void cleanAntennasFile(DateTime date) {
        WriteText.cleanFile(new File(AlertMessages.getString("RFID.PALLET.LOG.FILE.RESUME")), date, AlertMessages.getInteger("RFID.PALLET.LOG.NB_DAYS_TO_CHECK"));

    }

    /**
     * Checks whether the string supplied are present X times in the list, and
     * returns the name of antenna if so X =RFID.PALLET.LOG.NB_DAYS_TO_CHECK
     */
    private List<String> getAntennasNotReadForFourDays(DateTime lastDay) {
        List<String> antennasNameToCheck = newArrayList(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL"),
                AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL"));

        List<DateTime> daysToCheck = DaysUtils.getDayAndDaysBefore(lastDay, AlertMessages.getInteger("RFID.PALLET.LOG.NB_DAYS_TO_CHECK"), false);
        DateTimeFormatter fmt = ISODateTimeFormat.date();
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();

        List<String> allAntennasNotRead = new ArrayList<String>();
        for (DateTime day : daysToCheck) {
            for (String antennaName : antennasNameToCheck) {
                if (ReadText.isInTheFile(new File(AlertMessages.getString("RFID.PALLET.LOG.FILE.RESUME")), antennaName, day, fmt, comparator)) {
                    allAntennasNotRead.add(antennaName);
                }
            }
        }

        return filterAntennasEqualsDaysCount(allAntennasNotRead);
    }

    private List<String> filterAntennasEqualsDaysCount(List<String> allAntennasNotRead) {
        List<String> antennasNotReadForFourDays = new ArrayList<String>();
        if (Collections2.filter(allAntennasNotRead, isAntenna1).size() == AlertMessages.getInteger("RFID.PALLET.LOG.NB_DAYS_TO_CHECK")) {
            antennasNotReadForFourDays.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL"));
        }
        if (Collections2.filter(allAntennasNotRead, isAntenna2).size() == AlertMessages.getInteger("RFID.PALLET.LOG.NB_DAYS_TO_CHECK")) {
            antennasNotReadForFourDays.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL"));
        }
        if (Collections2.filter(allAntennasNotRead, isAntenna3).size() == AlertMessages.getInteger("RFID.PALLET.LOG.NB_DAYS_TO_CHECK")) {
            antennasNotReadForFourDays.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL"));
        }
        if (Collections2.filter(allAntennasNotRead, isAntenna4).size() == AlertMessages.getInteger("RFID.PALLET.LOG.NB_DAYS_TO_CHECK")) {
            antennasNotReadForFourDays.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL"));
        }
        return antennasNotReadForFourDays;
    }

    /**
     * Reads the resume file and checks which antennas have been read for the
     * supplied day.
     *
     * @param startDate
     * @return
     * @throws IOException
     */
    private List<String> getAntennasNotReadFromLogs(DateTime startDate) throws IOException {
        File file = new File(AlertMessages.getString("RFID.PALLET.LOG.DIRECTORY"));
        DateTimeFormatter fmt = ISODateTimeFormat.date();
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
        List<String> antennaNotRead = new ArrayList<String>();

        if (!ReadText.isInTheDirectory(file, AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1"), startDate, fmt, comparator)) {
            antennaNotRead.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL"));
        }

        if (!ReadText.isInTheDirectory(file, AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2"), startDate, fmt, comparator)) {
            antennaNotRead.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL"));
        }

        if (!ReadText.isInTheDirectory(file, AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3"), startDate, fmt, comparator)) {
            antennaNotRead.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL"));
        }

        if (!ReadText.isInTheDirectory(file, AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4"), startDate, fmt, comparator)) {
            antennaNotRead.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL"));
        }
        return antennaNotRead;
    }

    /**
     * Specifies whether an alert must be found
     *
     * @param dateTime
     * @return true if the day is beetween Tueday and Saturday
     */
    protected boolean generate(DateTime dateTime) {
        return !DaysUtils.isSunday(dateTime);
    }

}
