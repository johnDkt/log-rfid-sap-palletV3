package com.decathlon.log.rfid.pallet.utils;

import lombok.extern.log4j.Log4j;

import java.util.Properties;


@Log4j
public class RFIDProperties {

    private static Properties props;

    static {
        props = new Properties();
        try {
            props.load(ClassLoader.getSystemResourceAsStream("application.properties"));
        } catch (final Exception e) {
            log.error("Exception : ", e);
        }
    }

    private RFIDProperties() {
    }

    /**
     * Returns the Properties object instance.
     *
     * @param p Element of PROPERTIES enumeration.
     * @return properties object
     */
    public static String getValue(PROPERTIES p) {
        return props.getProperty(p.toString()) != null ? props.getProperty(p.toString()).trim() : null;
    }

    public static boolean stringStartsWithItemFromList(String inputString, String[] items) {
        for (final String item : items) {
            if (inputString.startsWith(item)) {
                return true;
            }
        }
        return false;
    }

    public enum PROPERTIES {

        /**
         * Temps de lecture pallet
         */
        READ_PALLET_TIMEOUT("READ_PALLET_TIMEOUT"),

        /**
         * Est-on en mode debug ?
         */
        DEBUG("DEBUG"),

        /**
         * Application server for WS calls.
         */
        NB_SGTIN("NB_SGTIN"),

        /**
         * Nom du mat.
         */
        MAT("MAT"),

        /**
         * Application server for WS calls.
         */
        WS_URL("WS_URL"),

        /**
         * Connection type for scanner (IP or COM).
         */
        CONNECTION_TYPE("CONNECTION_TYPE"),

        /**
         * Device type; reader or tunnel.
         */
        DEVICE_TYPE("DEVICE_TYPE"),


        DISPLAY_AUTH_FORM("DISPLAY_AUTH_FORM"),

        /**
         * Nb of seconds we show the parameter popup error
         */
        ERROR_PARAMETERS_POPUP_TIMEOUT("ERROR_PARAMETERS_POPUP_TIMEOUT"),

        /**
         * Nb of seconds we show load error popup
         */
        LOAD_ERROR_POPUP_TIMEOUT("LOAD_ERROR_POPUP_TIMEOUT"),

        /**
         * Nb of seconds we show error load truck content popup
         */
        TRUCK_CONTENT_ERROR_POPUP_TIMEOUT("TRUCK_CONTENT_ERROR_POPUP_TIMEOUT"),

        /**
         * Nb of seconds we show error load truck content popup
         */
        PALLET_CONTENT_ERROR_POPUP_TIMEOUT("PALLET_CONTENT_ERROR_POPUP_TIMEOUT"),

        /**
         * AS400 server.
         */
        AS400_SERVER("AS400_SERVER"),

        /**
         * AS400 profile name.
         */
        AS400_PROFILE_NAME("AS400_PROFILE_NAME"),

        /**
         * AS400 profile password.
         */
        AS400_PROFILE_PASSWORD("AS400_PROFILE_PASSWORD"),

        /**
         * WS_CALL_TIMEOUT
         */
        WS_CALL_TIMEOUT("WS_CALL_TIMEOUT"),

        /**
         * USE_SYSTEM_PROXIES system property
         */
        USE_SYSTEM_PROXIES("USE_SYSTEM_PROXIES"),

        /**
         * AUTHENTICATE_WITH_WS
         */
        AUTHENTICATE_WITH_WS("AUTHENTICATE_WITH_WS"),

        /**
         * READ_SYSTEM_IN
         */
        READ_SYSTEM_IN("READ_SYSTEM_IN"),

        AS400_LIBRARY("AS400_LIBRARY"),

        OUTPUT_DATA_DIR("OUTPUT_DATA_DIR"),

        SAVE_OUTPUT_TO_FILE("SAVE_OUTPUT_TO_FILE"),

        OUTPUT_SUFFIX_DATA_FILE("OUTPUT_SUFFIX_DATA_FILE"),

        READ_TIMEOUT("READ_TIMEOUT"),

        LANGUAGE("LANGUAGE"),

        AUTO_VALIDATION("AUTO_VALIDATION"),

        AUTO_VALIDATION_TIMEOUT("AUTO_VALIDATION_TIMEOUT");


        private String value;

        PROPERTIES(String v) {
            value = v;
        }

        /**
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return value;
        }
    }

}
