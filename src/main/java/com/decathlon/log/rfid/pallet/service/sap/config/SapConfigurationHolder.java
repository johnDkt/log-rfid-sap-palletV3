package com.decathlon.log.rfid.pallet.service.sap.config;

import com.decathlon.connectJavaIntegrator.utils.Utils;
import com.decathlon.log.rfid.pallet.utils.RFIDProperties;
import lombok.Getter;

@Getter
public class SapConfigurationHolder {

    private static SapConfigurationHolder instance;
    private final int timeout;
    private final String serviceRoot;
    private final String warehouse;
    private final String login;
    private final String password;
    private final String client;
    private final String lang;

    private final String DEFAULT_TIMEOUT_VALUE = "30000";
    private final String DEFAULT_LANGUAGE = "EN";
    private final String DEFAULT_SAP_LOGIN = "RFID_EWM";
    private final String DEFAULT_SAP_PASSWORD = "UkZJRFBBU1MwMQ==";
    private final String DEFAULT_SAP_CLIENT = "042";

    private SapConfigurationHolder() {
        this.serviceRoot = RFIDProperties.getValue(RFIDProperties.PROPERTIES.SAP_SERVICE_ROOT);
        this.warehouse = RFIDProperties.getValue(RFIDProperties.PROPERTIES.SAP_WAREHOUSE);

        this.login = checkValue(RFIDProperties.PROPERTIES.SAP_LOGIN, DEFAULT_SAP_LOGIN);
        this.password = checkValue(RFIDProperties.PROPERTIES.SAP_PASSWORD, DEFAULT_SAP_PASSWORD);
        this.timeout = checkTimeoutValue();
        this.client = checkValue(RFIDProperties.PROPERTIES.SAP_CLIENT, DEFAULT_SAP_CLIENT);
        this.lang = checkValue(RFIDProperties.PROPERTIES.LANGUAGE, DEFAULT_LANGUAGE);
    }

    private int checkTimeoutValue(){
       return Integer.parseInt(checkValue(RFIDProperties.PROPERTIES.SAP_TIMEOUT, DEFAULT_TIMEOUT_VALUE));
    }

    private String checkValue( RFIDProperties.PROPERTIES property, String defaultValue){
        String propertyValue = RFIDProperties.getValue(property);
        return Utils.isBlankOrEmpty(propertyValue) ? defaultValue : propertyValue;
    }

    public static SapConfigurationHolder getInstance() {
        if (instance == null) {
            synchronized (SapConfigurationHolder.class) {
                if (instance == null) {
                    instance = new SapConfigurationHolder();
                }
            }
        }
        return instance;
    }

}
