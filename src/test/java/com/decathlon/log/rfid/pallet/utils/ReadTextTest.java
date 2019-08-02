package com.decathlon.log.rfid.pallet.utils;

import com.decathlon.log.rfid.pallet.alert.message.AlertMessages;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReadTextTest {

    private static final String ANTENNA_4 = AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4");
    private static final String ANTENNA_1 = AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1");
    private static final String ANTENNA_2 = AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2");
    private static final String ANTENNA_NOT_PRESENT = "antenna: not present";
    private DateTime dateFromLog;
    private DateTime dateNotFound;
    private DateTimeFormatter fmt = ISODateTimeFormat.date();
    private DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();

    @Before
    public void setup() {
        dateFromLog = DateTime.now().withYear(2014).withMonthOfYear(06).withDayOfMonth(23);
        ;
        dateNotFound = DateTime.now().withYear(2013);
    }

    @Test
    public void antenna2NotReadInFile() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("log/log-rfid-pallet-antenna.log").getFile());
        assertFalse(ReadText.isInTheFile(file, ANTENNA_2, dateNotFound, fmt, comparator));
    }

    @Test
    public void antenna2IsReadInFile() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("log/log-rfid-pallet-antenna.log.1").getFile());
        assertTrue(ReadText.isInTheFile(file, ANTENNA_2, dateFromLog, fmt, comparator));
    }

    @Test
    public void antenna4IsReadInFile() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("log/log-rfid-pallet-antenna.log.4").getFile());
        assertTrue(ReadText.isInTheFile(file, ANTENNA_4, dateFromLog, fmt, comparator));
    }

    @Test
    public void antenna4IsReadInDirectory() throws Exception {
        URL url = ReadTextTest.class.getResource("/log");
        File file = new File(url.toURI());
        assertTrue(ReadText.isInTheDirectory(file, ANTENNA_4, dateFromLog, fmt, comparator));
    }

    @Test
    public void antenna4IsNotReadInDirectory() throws Exception {
        URL url = ReadTextTest.class.getResource("/log_no_antenna");
        File file = new File(url.toURI());
        assertFalse(ReadText.isInTheDirectory(file, ANTENNA_4, dateNotFound, fmt, comparator));
    }

    @Test
    public void antenna2IsReadInDirectory() throws Exception {
        URL url = ReadTextTest.class.getResource("/log");
        File file = new File(url.toURI());
        assertTrue(ReadText.isInTheDirectory(file, ANTENNA_1, dateFromLog, fmt, comparator));
    }

    @Test
    public void antennaNotReadInDirectory() throws Exception {
        URL url = ReadTextTest.class.getResource("/log");
        File file = new File(url.toURI());
        assertFalse(ReadText.isInTheDirectory(file, ANTENNA_NOT_PRESENT, dateNotFound, fmt, comparator));
    }

    @Test
    public void LogfileNameIsGood() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("log/log-rfid-pallet-antenna.log.1").getFile());
        assertTrue(ReadText.isLogFile(file.getName()));
    }

    @Test
    public void LogfileShortNameIsGood() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("log/log-rfid-pallet-antenna.log.1").getFile());
        assertTrue(ReadText.isLogFile(file.getName()));
    }

    @Test
    public void LogfileNameIsBad() throws Exception {

        File file = new File(getClass().getClassLoader().getResource("log/log-rfid-pallet-sgtin.log").getFile());
        assertFalse(ReadText.isLogFile(file.getName()));
    }

    @Test
    public void readNumberLineInFile() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("log/log-rfid-pallet-sgtin.log").getFile());
        org.junit.Assert.assertEquals(25, ReadText.countLine(file));

    }
}
