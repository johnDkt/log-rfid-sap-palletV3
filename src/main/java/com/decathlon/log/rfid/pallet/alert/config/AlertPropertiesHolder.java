package com.decathlon.log.rfid.pallet.alert.config;

import com.decathlon.log.rfid.pallet.alert.message.AlertProperties;
import com.decathlon.log.rfid.pallet.alert.message.AlertPropertiesEnum;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import java.util.List;

@Log4j
@Getter
public class AlertPropertiesHolder {

    private final int port = 25;
    private final String protocol = "smtp";
    private final String charset = "text/html; charset=utf-8";

    private String host;
    private String timeout;
    private String connectionTimeout;

    public AlertPropertiesHolder() {
        this.host = parseString(AlertPropertiesEnum.MAIL_HOST);
        this.timeout = parseString(AlertPropertiesEnum.MAIL_TIMEOUT);
        this.connectionTimeout = parseString(AlertPropertiesEnum.MAIL_CONNECTION_TIMEOUT);
    }

    protected String parseString(final AlertPropertiesEnum key) {
        final String value = AlertProperties.getInstance().getValue(key);
        if (Strings.isNullOrEmpty(value)) {
            log.error("Key " + key.toString() + " has null or empty value !");
            throw new RuntimeException("Key " + key.toString() + " has null or empty value !");
        }

        return value;
    }

    protected List<String> parsePropertyIntoList(final AlertPropertiesEnum key) {
        final String value = AlertProperties.getInstance().getValue(key);
        if (Strings.isNullOrEmpty(value)) {
            log.error("Key " + key.toString() + " has null or empty value !");
            throw new RuntimeException("Key " + key.toString() + " has null or empty value !");
        }

        return Lists.newArrayList(Splitter.on(';').trimResults().omitEmptyStrings().split(value));

    }


}
