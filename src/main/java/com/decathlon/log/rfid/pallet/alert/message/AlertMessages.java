package com.decathlon.log.rfid.pallet.alert.message;


import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class AlertMessages {
    private static final Logger log = Logger.getLogger(AlertMessages.class);
    private static final String BUNDLE_NAME = "alert";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);


    private AlertMessages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    public static String getString(String key, String[] args) {
        try {
            MessageFormat messageForm = new MessageFormat("");
            messageForm.applyPattern(RESOURCE_BUNDLE.getString(key));
            return messageForm.format(args);

        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    public static Integer getInteger(String key) {
        try {
            return Integer.parseInt(getString(key));

        } catch (NumberFormatException nfe) {
            log.warn("Error while formatting " + key + " to Integer", nfe);
            return -1;
        }
    }

}
