package com.decathlon.log.rfid.pallet.alert;

import com.decathlon.log.rfid.pallet.alert.config.AlertAntennaPropertiesHolder;
import com.decathlon.log.rfid.pallet.alert.config.AlertPropertiesHolder;
import com.decathlon.log.rfid.pallet.alert.entity.RecipientDto;
import com.decathlon.log.rfid.pallet.alert.message.AlertMessages;
import com.decathlon.log.rfid.pallet.alert.message.AlertProperties;
import com.decathlon.log.rfid.pallet.alert.service.BoAlert;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AlertMessages.class, BoAlert.class, Transport.class, AlertProperties.class})
public class BoAlertTest {

    @Mock
    private AlertPropertiesHolder alertPropertiesHolder;

    @Mock
    private AlertAntennaPropertiesHolder alertAntennaPropertiesHolder;

    @Test
    public void shouldSendEmail() throws Exception {
        PowerMockito.mockStatic(Transport.class);
        mockAlertPropertiesHolder();

        final BoAlert boAlert = new BoAlert();

        boAlert.sendEmail(
                new RecipientDto("from", "from@decathlon.com"),
                Lists.newArrayList(
                        new RecipientDto("to", "to@decathlon.com"),
                        new RecipientDto("to2", "to2@decathlon.com")
                ),
                "Subject", "Message"
        );

        final ArgumentCaptor<MimeMessage> mimeMessageArgumentCaptor =
                ArgumentCaptor.forClass(MimeMessage.class);
        PowerMockito.verifyStatic();
        Transport.send(mimeMessageArgumentCaptor.capture());

        assertEquals("Subject", mimeMessageArgumentCaptor.getValue().getSubject());
        assertEquals("Message", mimeMessageArgumentCaptor.getValue().getContent());

    }

    private void mockAlertPropertiesHolder() throws Exception {
        PowerMockito.whenNew(AlertPropertiesHolder.class).withAnyArguments().thenReturn(alertPropertiesHolder);

        when(alertPropertiesHolder.getCharset()).thenReturn("text/html; charset=utf-8");
        when(alertPropertiesHolder.getHost()).thenReturn("127.0.0.1");
        when(alertPropertiesHolder.getPort()).thenReturn(25);
        when(alertPropertiesHolder.getProtocol()).thenReturn("smtp");
        when(alertPropertiesHolder.getTimeout()).thenReturn("1000");
        when(alertPropertiesHolder.getConnectionTimeout()).thenReturn("3000");
    }

    @Test
    public void shouldSendMailIfRapportNotGenerate() throws Exception {
        PowerMockito.mockStatic(Transport.class);
        mockAlertPropertiesHolder();
        PowerMockito.whenNew(AlertAntennaPropertiesHolder.class).withAnyArguments().thenReturn(alertAntennaPropertiesHolder);

        when(alertAntennaPropertiesHolder.getTo()).thenReturn(Lists.newArrayList("to@decathlon.com", "to2@decathlon.com"));
        when(alertAntennaPropertiesHolder.getFrom()).thenReturn("DBCWNTU02.noreply@decathlon.com");
        when(alertAntennaPropertiesHolder.getContent()).thenReturn("Content");
        when(alertAntennaPropertiesHolder.getSubject()).thenReturn("Subject");

        final BoAlert boAlert = new BoAlert();
        boAlert.sendRapportAlertAntenna(DateTime.now(), Lists.newArrayList("antenna11", "antenna22"));

        final ArgumentCaptor<MimeMessage> mimeMessageArgumentCaptor =
                ArgumentCaptor.forClass(MimeMessage.class);
        PowerMockito.verifyStatic();
        Transport.send(mimeMessageArgumentCaptor.capture());

        assertEquals("Subject", mimeMessageArgumentCaptor.getValue().getSubject());
        assertEquals("Content", mimeMessageArgumentCaptor.getValue().getContent());

        final InternetAddress[] recipients = (InternetAddress[]) mimeMessageArgumentCaptor.getValue().getRecipients(Message.RecipientType.TO);
        assertEquals("to@decathlon.com", recipients[0].getAddress());
        assertEquals("to2@decathlon.com", recipients[1].getAddress());

        final InternetAddress[] from = (InternetAddress[]) mimeMessageArgumentCaptor.getValue().getFrom();
        assertEquals("DBCWNTU02.noreply@decathlon.com", from[0].getAddress());
    }

}
