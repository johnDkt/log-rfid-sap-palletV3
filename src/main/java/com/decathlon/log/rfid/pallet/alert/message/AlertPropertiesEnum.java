package com.decathlon.log.rfid.pallet.alert.message;

import lombok.Getter;

public enum AlertPropertiesEnum {

    MAIL_HOST("mail.smtp.host"),
    MAIL_TIMEOUT("mail.smtp.timeout"),
    MAIL_CONNECTION_TIMEOUT("mail.smtp.connectiontimeout"),

    MAIL_FROM("MAIL_HOSTNAME"),
    MAIL_TO("MAIL_SEND_TO"),
    MAIL_WAREHOUSE("MAIL_WAREHOUSE"),
    MAIL_SUBJECT_ALERT_ANTENNA("MAIL_SUBJECT_ALERT_ANTENNA"),
    MAIL_CONTENT_ALERT_ANTENNA("MAIL_CONTENT_ALERT_ANTENNA");

    @Getter
    private String value;

    AlertPropertiesEnum(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
