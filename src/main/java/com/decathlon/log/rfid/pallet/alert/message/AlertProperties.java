package com.decathlon.log.rfid.pallet.alert.message;

import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.util.Properties;

@Log4j
public class AlertProperties {

    private static AlertProperties instance;
    private Properties properties;

    private AlertProperties() {
        final String alertFilename = "alert.properties";
        properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemResourceAsStream(alertFilename));
        } catch (final IOException e) {
            log.error("Impossible to read " + alertFilename + " !", e);
            throw new RuntimeException("Impossible to read " + alertFilename + " !", e);
        }
    }

    public static AlertProperties getInstance() {
        if (instance == null) {
            synchronized (AlertProperties.class) {
                if (instance == null) {
                    instance = new AlertProperties();
                }
            }
        }
        return instance;
    }

    public String getValue(final AlertPropertiesEnum key) {
        return properties.getProperty(key.toString());
    }

}
