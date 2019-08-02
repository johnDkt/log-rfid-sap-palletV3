package com.decathlon.log.rfid.pallet.alert.service;

import com.decathlon.log.rfid.pallet.alert.config.AlertAntennaPropertiesHolder;
import com.decathlon.log.rfid.pallet.alert.config.AlertPropertiesHolder;
import com.decathlon.log.rfid.pallet.alert.entity.RecipientDto;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class BoAlert {

    private Session session;
    private AlertPropertiesHolder alertPropertiesHolder;

    public BoAlert() {
        this.alertPropertiesHolder = new AlertPropertiesHolder();
        this.session = createMailerSession(alertPropertiesHolder);
    }

    private Session createMailerSession(final AlertPropertiesHolder alertPropertiesHolder) {
        final Properties props = new Properties();
        props.setProperty("mail.transport.protocol", alertPropertiesHolder.getProtocol());
        props.setProperty("mail.smtp.timeout", alertPropertiesHolder.getTimeout());
        props.setProperty("mail.smtp.host", String.valueOf(alertPropertiesHolder.getHost()));
        props.setProperty("mail.smtp.port", String.valueOf(alertPropertiesHolder.getPort()));
        props.setProperty("mail.smtp.connectiontimeout", alertPropertiesHolder.getConnectionTimeout());
        props.setProperty("mail.mime.charset", alertPropertiesHolder.getCharset());
        return Session.getDefaultInstance(props);
    }

    public void sendRapportAlertAntenna(final DateTime date, final Collection<String> antennas) throws UnsupportedEncodingException, MessagingException {
        final AlertAntennaPropertiesHolder alertAntennaPropertiesHolder = new AlertAntennaPropertiesHolder(date, antennas);

        final List<RecipientDto> tos = Lists.transform(alertAntennaPropertiesHolder.getTo(), new Function<String, RecipientDto>() {
            @Override
            public RecipientDto apply(final String mailTo) {
                return new RecipientDto(mailTo, mailTo);
            }
        });

        this.sendEmail(new RecipientDto(alertAntennaPropertiesHolder.getFrom(), alertAntennaPropertiesHolder.getFrom()),
                tos, alertAntennaPropertiesHolder.getSubject(), alertAntennaPropertiesHolder.getContent());

    }

    public void sendEmail(final RecipientDto from, final List<RecipientDto> tos,
                          final String subject, final String content) throws UnsupportedEncodingException, MessagingException {

        final Address[] addresses = createAddresses(tos);

        final MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(from.getAddress()));
        mimeMessage.setRecipients(Message.RecipientType.TO, addresses);
        mimeMessage.setSubject(subject, alertPropertiesHolder.getCharset());
        mimeMessage.setContent(content, alertPropertiesHolder.getCharset());

        Transport.send(mimeMessage);

    }

    private Address[] createAddresses(final List<RecipientDto> tos) throws UnsupportedEncodingException, AddressException {
        final Address[] addresses = new Address[tos.size()];

        for (int i = 0; i < tos.size(); i++) {
            RecipientDto to = tos.get(i);
            addresses[i] = new InternetAddress(to.getAddress());
        }

        return addresses;

    }

}