package com.decathlon.log.rfid.pallet.resources;

import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 */
public class ResourceManager {
    private static ResourceManager instance;
    private static Logger LOGGER = Logger.getLogger(ResourceManager.class);


    // default value
    private static Locale userLocale = Locale.ENGLISH;

    // Instance class members
    private static ResourceBundle resourceBundle;

    private ResourceManager(Locale locale) {
        super();
        try {
            resourceBundle = ResourceBundle.getBundle("messages.messages", locale);
        } catch (MissingResourceException e) {
            LOGGER.error("Error while instance ResourceManager with locale " + locale.toString(), e);
        }
    }

    /**
     * Returns a singleton of the resource manager.
     *
     * @return an instance of the resource manager.
     */
    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager(userLocale);
        }
        return instance;
    }

    /**
     * Returns a singleton of the resource manager.
     *
     * @return an instance of the resource manager.
     */
    public static ResourceManager getInstance(Locale locale) {
        if (instance == null) {
            instance = new ResourceManager(locale);
        }
        return instance;
    }

    /**
     * @param key String the input to use to find the value
     * @return String    the value of the key or'!' + key + '!' if it is undefined.
     */
    public String getString(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    /**
     * Get the value of the key with args
     *
     * @param key  - String the input to use to find the value
     * @param args - args in messages
     * @return -the value of the key with args
     */
    public String getString(String key, String[] args) {
        try {
            MessageFormat messageForm = new MessageFormat("");
            messageForm.applyPattern(resourceBundle.getString(key));
            return messageForm.format(args);

        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

}