package com.decathlon.log.rfid.pallet.utils;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.io.Files;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class WriteText {

    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static Logger LOGGER = Logger.getLogger(WriteText.class);

    public static void writeInFile(File file, java.util.Collection<String> ListSentence, DateTime date) throws IOException {
        DateTimeFormatter fmtDate = ISODateTimeFormat.date();

        Joiner joiner = Joiner.on("|");
        Files.append(fmtDate.print(date) + "|" + joiner.join(ListSentence), file, CHARSET);
        Files.append(System.getProperty("line.separator"), file, CHARSET);
    }

    public static void cleanFile(File file, DateTime date, Integer numberOfdaysToKeep) {

        LOGGER.info(" Try to clean file : " + file.getName() + " to keep: " + numberOfdaysToKeep + " days . Starting date :" + date.toString());
        List<DateTime> daysToKeep = DaysUtils.getDayAndDaysBefore(date, numberOfdaysToKeep, false);
        StringBuffer linesToKeep = new StringBuffer();


        try {
            List<String> lines = Files.readLines(file, CHARSET);
            clearFile(file);
            for (String line : lines) {
                String inputDate = line.substring(0, 10);
                Optional<DateTime> formattedDate = formatDate(inputDate);
                if (formattedDate.isPresent() && DaysUtils.dateOnlyIsInArray(daysToKeep, formattedDate.get())) {
                    linesToKeep.append(line).append(System.getProperty("line.separator"));
                }
            }
            Files.append(linesToKeep.toString(), file, CHARSET);
            LOGGER.info("File cleared");
        } catch (IOException e) {
            LOGGER.error("Error while cleaning file ", e);
        }


    }


    private static Optional<DateTime> formatDate(String inputDate) {
        DateTimeFormatter fmt = ISODateTimeFormat.date();
        try {
            return Optional.fromNullable(fmt.parseDateTime(inputDate));
        } catch (IllegalArgumentException iae) {
            LOGGER.info("Error while formatting date : ", iae);
            return Optional.absent();
        }
    }

    private static void clearFile(File file) throws IOException {
        Files.write(new byte[]{}, file);
        LOGGER.info("file " + file.getAbsolutePath() + " cleared");
    }
}
