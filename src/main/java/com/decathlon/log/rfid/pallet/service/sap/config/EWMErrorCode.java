package com.decathlon.log.rfid.pallet.service.sap.config;

import lombok.Getter;

public enum EWMErrorCode {

    HU_NOT_FOUND("/SCWM/HU_WM/015");

    @Getter
    private String errorCode;

    EWMErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return errorCode;
    }
}
