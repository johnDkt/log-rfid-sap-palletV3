package com.decathlon.log.rfid.pallet.utils;

import com.decathlon.log.rfid.pallet.alert.message.AlertMessages;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class WriteTextTest {

    private DateTime dateMonday = DateTime.now().withDayOfWeek(DateTimeConstants.MONDAY);
    private DateTimeFormatter fmtDate = ISODateTimeFormat.date();
    private File file;

    @Before
    public void setUp() throws Exception {
        URL url = ReadTextTest.class.getResource("/log");
        file = new File(new URL(url + "/resume.log").toURI());
    }

    @After
    public void tearDown() throws Exception {
        file.delete();
    }

    @Test
    public void testWriteInFile() throws Exception {


        List<String> sentence = new ArrayList<String>();
        sentence.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL"));
        sentence.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL"));

        WriteText.writeInFile(file, sentence, dateMonday);

        String lines = Files.readFirstLine(file, Charsets.UTF_8);
        String expected = fmtDate.print(dateMonday) + "|" + AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL") + "|" + AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL");
        Assert.assertEquals(expected, lines);
    }

    @Test
    public void cleanFile() throws Exception {

        WriteText.writeInFile(file, newArrayList(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), dateMonday.minusDays(5));
        WriteText.writeInFile(file, newArrayList(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), dateMonday.minusDays(4));
        WriteText.writeInFile(file, newArrayList(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL")), dateMonday.minusDays(3));
        WriteText.writeInFile(file, newArrayList(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), dateMonday.minusDays(2));
        WriteText.writeInFile(file, newArrayList(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL")), dateMonday);

        String lines = Files.readFirstLine(file, Charsets.UTF_8);
        String expected = fmtDate.print(dateMonday.minusDays(5)) + "|" + AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL") + "|" + AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL");
        Assert.assertEquals(expected, lines);

        WriteText.cleanFile(file, dateMonday, AlertMessages.getInteger("RFID.PALLET.LOG.NB_DAYS_TO_CHECK"));
        lines = Files.readFirstLine(file, Charsets.UTF_8);
        expected = fmtDate.print(dateMonday.minusDays(4)) + "|" + AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL") + "|" + AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL");
        Assert.assertEquals(expected, lines);
    }

}
