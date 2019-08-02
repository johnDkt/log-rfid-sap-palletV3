package com.decathlon.log.rfid.pallet.utils;

import com.decathlon.log.rfid.pallet.alert.message.AlertMessages;
import com.google.common.collect.FluentIterable;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Utils class about reading file
 *
 * @author Z26SBILL
 */
public class ReadText {

    public static final Charset CHARSET = Charset.forName("UTF-8");
    private static Logger LOGGER = Logger.getLogger(ReadText.class);


    /**
     * Allows to know if a sentence is present in a file at this dateTime
     *
     * @param sentence   - the phrase
     * @param fileName   - the name of the file
     * @param dateTime   - the dateTime
     * @param formatter  - formatter to read Date from file
     * @param comparator - comparator to distinc if two date are egual
     * @return true if the sentence is present a this dateTime with this formatter
     */
    public static boolean isInTheFile(File file, final String sentence, final DateTime dateTime, final DateTimeFormatter formatter, final DateTimeComparator comparator) {

        Boolean contains = false;
        try {
            LOGGER.info(" Try to Read file : " + file.getName() + " to find: " + sentence);
            contains = Files.readLines(file, CHARSET, new LineProcessor<Boolean>() {

                Boolean containsGivenText = false;

                @Override
                public Boolean getResult() {
                    return containsGivenText;
                }

                @Override
                public boolean processLine(final String line) {

                    DateTime dateFromLog;

                    try {
                        String inputDate = line.substring(0, 10);
                        dateFromLog = formatter.parseDateTime(inputDate);
                    } catch (IllegalArgumentException illegalArgumentException) {
                        LOGGER.debug("Error while readLine in ", illegalArgumentException);
                        return true; //if an arror appeared about date parsing, we continue reading
                    } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                        LOGGER.debug("Error while readLine in ", indexOutOfBoundsException);
                        return true; //if an arror appeared about date parsing, we continue reading
                    }


                    if (comparator.compare(dateFromLog, dateTime) == 0 && line.contains(sentence)) {
                        containsGivenText = true;
                        LOGGER.info("This sentence :" + sentence + " was found");
                        return false; // if sentence found, so we stop reading

                    } else {
                        return true;
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.error("Error while readLine in " + file.getName(), e);
            return false;
        }

        return contains;
    }


    /**
     * Allows to know if a sentence is present in some file in the directory
     *
     * @param pattern       - the phrase
     * @param directoryName - the name of the directoryName
     * @param dateTime      - dateTime log
     * @param comparator    - comparator to distinc if two date are egual
     * @param sentence      - the phrase
     * @return true if the pattern is found in the directory
     * @throws IOException
     * @pram fmt - the date's formatter
     */
    public static boolean isInTheDirectory(File directoryName, String sentence, DateTime dateTime, DateTimeFormatter fmt, DateTimeComparator comparator) throws IOException {

        FluentIterable<File> files = Files.fileTreeTraverser().breadthFirstTraversal(directoryName);
        for (File file : files) {
            if (!file.isDirectory() && isLogFile(file.getName())) {
                if (ReadText.isInTheFile(file, sentence, dateTime, fmt, comparator)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Allow to know if a fileName is a log file for antenna
     *
     * @param fileName - the filename
     * @return true if the file name begin by the pattern, else false
     */
    public static boolean isLogFile(String fileName) {
        return fileName.startsWith(AlertMessages.getString("RFID.PALLET.LOG.NAME.FILE"));
    }


    public static int countLine(File file) throws IOException {
        return Files.readLines(file, CHARSET, new LineProcessor<Integer>() {
            int count = 0;

            @Override
            public Integer getResult() {
                return count;
            }

            @Override
            public boolean processLine(String arg0) throws IOException {
                count++;
                return true;
            }
        });
    }


}
