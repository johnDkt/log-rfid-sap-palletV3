package com.decathlon.log.rfid.pallet.autovalidation;

import com.decathlon.log.rfid.pallet.utils.RFIDProperties;
import lombok.Getter;

@Getter
public class AutoValidationPropertiesHolder {

    private static AutoValidationPropertiesHolder instance;
    private final boolean enable;
    private final int timeout;

    private AutoValidationPropertiesHolder() {
        this.enable = Boolean.parseBoolean(RFIDProperties.getValue(RFIDProperties.PROPERTIES.AUTO_VALIDATION));
        this.timeout = Integer.parseInt(RFIDProperties.getValue(RFIDProperties.PROPERTIES.AUTO_VALIDATION_TIMEOUT));
    }

    public static AutoValidationPropertiesHolder getInstance() {
        if (instance == null) {
            synchronized (AutoValidationPropertiesHolder.class) {
                if (instance == null) {
                    instance = new AutoValidationPropertiesHolder();
                }
            }
        }
        return instance;
    }


}
