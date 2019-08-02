package com.decathlon.log.rfid.pallet.alert;

import com.decathlon.log.rfid.pallet.alert.message.AlertMessages;
import com.decathlon.log.rfid.pallet.alert.service.BoAlert;
import com.decathlon.log.rfid.pallet.utils.ReadText;
import com.decathlon.log.rfid.pallet.utils.WriteText;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ReadText.class, WriteText.class})
public class GenerateAlertTest {

    @Mock
    private BoAlert alert;

    private GenerateAntennaAlert generateAlert;
    private DateTime dateMonday = DateTime.now().withDayOfWeek(DateTimeConstants.MONDAY);


    @Before
    public void setUp() throws Exception {
        generateAlert = new GenerateAntennaAlert();
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(ReadText.class);

    }


    @Test
    public void sendMailIfAntenna1NotReadForFourDays() throws Exception {

        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL")), eq(dateMonday), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL")), eq(dateMonday.minusDays(2)), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL")), eq(dateMonday.minusDays(3)), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL")), eq(dateMonday.minusDays(4)), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);

        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);

        List<String> antennaNotRead = new ArrayList<String>();
        antennaNotRead.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL"));

        generateAlert.lauch(alert, dateMonday);
        Mockito.verify(alert).sendRapportAlertAntenna(any(DateTime.class), Matchers.eq(antennaNotRead));
        PowerMockito.verifyStatic(Mockito.times(16));
        ReadText.isInTheFile(any(File.class), any(String.class), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class));
    }

    @Test
    public void sendMailIfAntenna2NotReadForFourDays() throws Exception {
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), eq(dateMonday), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), eq(dateMonday.minusDays(2)), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), eq(dateMonday.minusDays(3)), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), eq(dateMonday.minusDays(4)), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);

        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);

        List<String> antennaNotRead = new ArrayList<String>();
        antennaNotRead.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL"));

        generateAlert.lauch(alert, dateMonday);
        Mockito.verify(alert).sendRapportAlertAntenna(any(DateTime.class), Matchers.eq(antennaNotRead));
        PowerMockito.verifyStatic(Mockito.times(16));
        ReadText.isInTheFile(any(File.class), any(String.class), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class));
    }

    @Test
    public void sendMailIfAntenna3NotReadForFourDays() throws Exception {
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL")), eq(dateMonday), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL")), eq(dateMonday.minusDays(2)), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL")), eq(dateMonday.minusDays(3)), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL")), eq(dateMonday.minusDays(4)), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);

        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);

        List<String> antennaNotRead = new ArrayList<String>();
        antennaNotRead.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL"));

        generateAlert.lauch(alert, dateMonday);
        Mockito.verify(alert).sendRapportAlertAntenna(any(DateTime.class), Matchers.eq(antennaNotRead));
        PowerMockito.verifyStatic(Mockito.times(16));
        ReadText.isInTheFile(any(File.class), any(String.class), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class));
    }

    @Test
    public void sendMailIfAntenna4NotReadForFourDays() throws Exception {
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL")), eq(dateMonday), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL")), eq(dateMonday.minusDays(2)), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL")), eq(dateMonday.minusDays(3)), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL")), eq(dateMonday.minusDays(4)), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);

        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);


        List<String> antennaNotRead = new ArrayList<String>();
        antennaNotRead.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL"));

        generateAlert.lauch(alert, dateMonday);
        Mockito.verify(alert).sendRapportAlertAntenna(any(DateTime.class), Matchers.eq(antennaNotRead));
        PowerMockito.verifyStatic(Mockito.times(16));
        ReadText.isInTheFile(any(File.class), any(String.class), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class));
    }


    @Test
    public void notContainsInMailIfAntenna1ReadInFourDays() throws Exception {
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);

        List<String> antennaNotRead = newArrayList(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL"));

        generateAlert.lauch(alert, dateMonday);
        Mockito.verify(alert).sendRapportAlertAntenna(any(DateTime.class), Matchers.eq(antennaNotRead));
        PowerMockito.verifyStatic(Mockito.times(16));
        ReadText.isInTheFile(any(File.class), any(String.class), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class));
    }

    @Test
    public void notContainsInMailIfAntenna2ReadInFourDays() throws Exception {
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);

        List<String> antennaNotRead = newArrayList(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL"));

        generateAlert.lauch(alert, dateMonday);
        Mockito.verify(alert).sendRapportAlertAntenna(any(DateTime.class), Matchers.eq(antennaNotRead));
        PowerMockito.verifyStatic(Mockito.times(16));
        ReadText.isInTheFile(any(File.class), any(String.class), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class));
    }

    @Test
    public void notContainsInMailIfAntenna3ReadInFourDays() throws Exception {
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);

        List<String> antennaNotRead = newArrayList(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL"));

        generateAlert.lauch(alert, dateMonday);
        Mockito.verify(alert).sendRapportAlertAntenna(any(DateTime.class), Matchers.eq(antennaNotRead));
        ;
        PowerMockito.verifyStatic(Mockito.times(16));
        ReadText.isInTheFile(any(File.class), any(String.class), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class));
    }

    @Test
    public void notContainsInMailIfAntenna4ReadInFourDays() throws Exception {
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);

        List<String> antennaNotRead = newArrayList(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL"), AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL"));

        generateAlert.lauch(alert, dateMonday);
        Mockito.verify(alert).sendRapportAlertAntenna(any(DateTime.class), Matchers.eq(antennaNotRead));
        PowerMockito.verifyStatic(Mockito.times(16));
        ReadText.isInTheFile(any(File.class), any(String.class), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void notSendInMailAntennaListIsEmpty() throws Exception {
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);

        generateAlert.lauch(alert, dateMonday);
        Mockito.verify(alert, never()).sendRapportAlertAntenna(any(DateTime.class), Matchers.anyCollection());
        PowerMockito.verifyStatic(Mockito.times(16));
        ReadText.isInTheFile(any(File.class), any(String.class), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void doNotsendMailIfAntenna1isNotReadOnlyOneDay() throws Exception {
        DateTime lastDay = dateMonday;
        DateTime lasdayMinus2 = dateMonday.minusDays(2);
        DateTime lasdayMinus3 = dateMonday.minusDays(3);
        DateTime lasdayMinus4 = dateMonday.minusDays(4);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), Matchers.eq(lastDay), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), Matchers.eq(lastDay), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), Matchers.eq(lastDay), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), Matchers.eq(lastDay), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), Matchers.eq(lasdayMinus2), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), Matchers.eq(lasdayMinus2), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), Matchers.eq(lasdayMinus2), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), Matchers.eq(lasdayMinus2), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), Matchers.eq(lasdayMinus3), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), Matchers.eq(lasdayMinus3), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), Matchers.eq(lasdayMinus3), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), Matchers.eq(lasdayMinus3), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), Matchers.eq(lasdayMinus4), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), Matchers.eq(lasdayMinus4), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), Matchers.eq(lasdayMinus4), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), Matchers.eq(lasdayMinus4), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);


        generateAlert.lauch(alert, dateMonday);
        Mockito.verify(alert, never()).sendRapportAlertAntenna(any(DateTime.class), Matchers.anyCollection());
        PowerMockito.verifyStatic(Mockito.times(16));
        ReadText.isInTheFile(any(File.class), any(String.class), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void DoNotsendMailIfAntenna1isReadOnlyOneDayAndAntenna2IsReadOnly2days() throws Exception {
        DateTime lastDay = dateMonday;
        DateTime lasdayMinus2 = dateMonday.minusDays(2);
        DateTime lasdayMinus3 = dateMonday.minusDays(3);
        DateTime lasdayMinus4 = dateMonday.minusDays(4);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), Matchers.eq(lastDay), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), Matchers.eq(lastDay), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), Matchers.eq(lastDay), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), Matchers.eq(lastDay), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), Matchers.eq(lasdayMinus2), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), Matchers.eq(lasdayMinus2), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), Matchers.eq(lasdayMinus2), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), Matchers.eq(lasdayMinus2), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), Matchers.eq(lasdayMinus3), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), Matchers.eq(lasdayMinus3), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), Matchers.eq(lasdayMinus3), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), Matchers.eq(lasdayMinus3), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), Matchers.eq(lasdayMinus4), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), Matchers.eq(lasdayMinus4), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), Matchers.eq(lasdayMinus4), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheFile(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), Matchers.eq(lasdayMinus4), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);


        List<String> antennaNotRead = new ArrayList<String>();

        generateAlert.lauch(alert, dateMonday);
        Mockito.verify(alert, never()).sendRapportAlertAntenna(any(DateTime.class), Matchers.anyCollection());
        PowerMockito.verifyStatic(Mockito.times(16));
        ReadText.isInTheFile(any(File.class), any(String.class), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void notSendMailIfOnSunday() throws Exception {
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        DateTime dateTimeSunday = DateTime.now().withDayOfWeek(DateTimeConstants.SUNDAY);

        generateAlert.lauch(alert, dateTimeSunday);
        Mockito.verify(alert, never()).sendRapportAlertAntenna(any(DateTime.class), anyList());

    }


    @Test
    public void notGenerateOnMondayTest() throws Exception {
        DateTime dateTime = DateTime.now().withDayOfWeek(DateTimeConstants.MONDAY);
        assertTrue(generateAlert.generate(dateTime));
    }

    @Test
    public void generateOnTuesdayTest() throws Exception {
        DateTime dateTime = DateTime.now().withDayOfWeek(DateTimeConstants.TUESDAY);
        assertTrue(generateAlert.generate(dateTime));
    }

    @Test
    public void generateOnWednesdayTest() throws Exception {
        DateTime dateTime = DateTime.now().withDayOfWeek(DateTimeConstants.WEDNESDAY);
        assertTrue(generateAlert.generate(dateTime));
    }

    @Test
    public void generateOnThursdayTest() throws Exception {
        DateTime dateTime = DateTime.now().withDayOfWeek(DateTimeConstants.THURSDAY);
        assertTrue(generateAlert.generate(dateTime));
    }

    @Test
    public void generateOnFridayTest() throws Exception {
        DateTime dateTime = DateTime.now().withDayOfWeek(DateTimeConstants.FRIDAY);
        assertTrue(generateAlert.generate(dateTime));
    }

    @Test
    public void generateOnSaturdayTest() throws Exception {
        DateTime dateTime = DateTime.now().withDayOfWeek(DateTimeConstants.SATURDAY);
        assertTrue(generateAlert.generate(dateTime));
    }


    @Test
    public void generateOnSundayTest() throws Exception {
        DateTime dateTime = DateTime.now().withDayOfWeek(DateTimeConstants.SUNDAY);
        assertFalse(generateAlert.generate(dateTime));
    }


    @Test
    public void writeInFileIfAntenna1NotRead() throws Exception {

        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);

        List<String> antennaNotRead = new ArrayList<String>();
        antennaNotRead.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1.MAIL"));


        PowerMockito.mockStatic(WriteText.class);
        PowerMockito.doNothing().when(WriteText.class);
        WriteText.writeInFile(any(File.class), anyListOf(String.class), any(DateTime.class));

        generateAlert.lauch(alert, dateMonday);

        PowerMockito.verifyStatic(Mockito.times(1));
        WriteText.writeInFile(any(File.class), Matchers.eq(antennaNotRead), Matchers.eq(dateMonday));

    }

    @Test
    public void writeInFileIfAntenna2NotRead() throws Exception {

        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);

        List<String> antennaNotRead = new ArrayList<String>();
        antennaNotRead.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2.MAIL"));


        PowerMockito.mockStatic(WriteText.class);
        PowerMockito.doNothing().when(WriteText.class);
        WriteText.writeInFile(any(File.class), anyListOf(String.class), any(DateTime.class));

        generateAlert.lauch(alert, dateMonday);

        PowerMockito.verifyStatic(Mockito.times(1));
        WriteText.writeInFile(any(File.class), Matchers.eq(antennaNotRead), Matchers.eq(dateMonday));

    }

    @Test
    public void writeInFileIfAntenna3NotRead() throws Exception {

        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);

        List<String> antennaNotRead = new ArrayList<String>();
        antennaNotRead.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3.MAIL"));


        PowerMockito.mockStatic(WriteText.class);
        PowerMockito.doNothing().when(WriteText.class);
        WriteText.writeInFile(any(File.class), anyListOf(String.class), any(DateTime.class));

        generateAlert.lauch(alert, dateMonday);

        PowerMockito.verifyStatic(Mockito.times(1));
        WriteText.writeInFile(any(File.class), Matchers.eq(antennaNotRead), Matchers.eq(dateMonday));

    }

    @Test
    public void writeInFileIfAntenna4NotRead() throws Exception {

        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);

        List<String> antennaNotRead = new ArrayList<String>();
        antennaNotRead.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL"));


        PowerMockito.mockStatic(WriteText.class);
        PowerMockito.doNothing().when(WriteText.class);
        WriteText.writeInFile(any(File.class), anyListOf(String.class), any(DateTime.class));

        generateAlert.lauch(alert, dateMonday);

        PowerMockito.verifyStatic(Mockito.times(1));
        WriteText.writeInFile(any(File.class), Matchers.eq(antennaNotRead), Matchers.eq(dateMonday));

    }

    @Test
    public void cleanFileShouldBeCalled() throws Exception {

        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA1")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA2")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA3")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(true);
        when(ReadText.isInTheDirectory(any(File.class), eq(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4")), any(DateTime.class), any(DateTimeFormatter.class), any(DateTimeComparator.class))).thenReturn(false);

        List<String> antennaNotRead = new ArrayList<String>();
        antennaNotRead.add(AlertMessages.getString("RFID.PALLET.LOG.ANTENNA4.MAIL"));


        PowerMockito.mockStatic(WriteText.class);
        PowerMockito.doNothing().when(WriteText.class);
        WriteText.writeInFile(any(File.class), anyListOf(String.class), any(DateTime.class));

        generateAlert.lauch(alert, dateMonday);

        PowerMockito.verifyStatic(Mockito.times(1));
        WriteText.cleanFile(any(File.class), any(DateTime.class), any(Integer.class));

    }
}
