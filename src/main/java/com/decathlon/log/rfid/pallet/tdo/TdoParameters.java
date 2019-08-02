package com.decathlon.log.rfid.pallet.tdo;

import com.decathlon.log.rfid.pallet.constants.Constants;

import java.util.HashMap;
import java.util.Map;


public class TdoParameters {

    private String searchId;

    private String displayValue;

    private void buildSearchId() {
        displayValue = searchId;
    }

    public String getSearchId() {
        return displayValue;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
        buildSearchId();
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public Map<String, String> checkErrors() {
        Map<String, String> errorsMap = new HashMap<String, String>();

        if (searchId == null || searchId.trim().length() == 0) {
            this.addError(errorsMap, "id", "mandatory");
        } else if (searchId.trim().length() < Constants.NUM_UAT_LENGTH) {
            this.addError(errorsMap, "id", "mandatory");
        }

        return errorsMap;
    }

    private void addError(Map<String, String> errorsMap, String field, String value) {
        errorsMap.put(field, value);
    }

    public boolean hasErrors(Map<String, String> errorMap) {
        return errorMap != null && !errorMap.isEmpty();
    }
}
