package com.decathlon.log.rfid.pallet.main;

public enum RFIDPalletSessionKeys {

    SESSION_PARAMETERS_KEY("SESSION.PARAMETERS"),
    SESSION_CONNECTED_USER("SESSION.USER"),
    SESSION_SCAN_PANEL_MANAGER("SESSION.SCAN_PANEL_MANAGER"),
    SESSION_COMMON_PREVIOUS_RELEASED_TIME("SESSION_COMMON_PREVIOUS_RELEASED_TIME"),
    LISTE_ARTICLES_THEORIQUE("SESSION_ITEMS_LIST_KEY"),
    SESSION_SAVED_INVENTORY("SESSION_SAVED_INVENTORY"),;

    private String value;

    RFIDPalletSessionKeys(String v) {
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
