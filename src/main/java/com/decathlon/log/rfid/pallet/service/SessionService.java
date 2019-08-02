package com.decathlon.log.rfid.pallet.service;

import com.decathlon.log.rfid.pallet.tdo.TdoParameters;
import com.decathlon.log.rfid.pallet.tdo.TdoSave;
import com.decathlon.log.rfid.pallet.main.RFIDPalletSessionKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SessionService {

    private static SessionService instance;
    private HashMap<String, Object> sessionMap;

    private SessionService() {
        this.sessionMap = new HashMap<String, Object>();
    }

    public static SessionService getInstance() {
        synchronized (SessionService.class) {
            if (instance == null) {
                instance = new SessionService();
            }
            return instance;
        }
    }

    public void storeInSession(final RFIDPalletSessionKeys key, final Object value) {
        sessionMap.put(key.toString(), value);
    }

    public void removeFromSession(final RFIDPalletSessionKeys key) {
        sessionMap.remove(key.toString());
    }

    public <T> T retrieveFromSession(final RFIDPalletSessionKeys key, final Class<T> clazz) {
        return clazz.cast(sessionMap.get(key.toString()));
    }

    public <T> List<T> retrieveListFromSession(final RFIDPalletSessionKeys key, final Class<T> clazz) {
        final List list = (List) sessionMap.get(key.toString());
        final List<T> newList = new ArrayList<T>();
        for (final Object itObject : list) {
            newList.add(clazz.cast(itObject));
        }
        return newList;
    }

    public void storeParameters(final TdoParameters tdoParameters) {
        this.storeInSession(RFIDPalletSessionKeys.SESSION_PARAMETERS_KEY, tdoParameters);
    }

    public TdoParameters getParameters() {
        return this.retrieveFromSession(RFIDPalletSessionKeys.SESSION_PARAMETERS_KEY, TdoParameters.class);
    }

    public TdoSave getSave() {
        return this.retrieveFromSession(RFIDPalletSessionKeys.SESSION_SAVED_INVENTORY, TdoSave.class);
    }

    public void setSave(final TdoSave tdoSave) {
        this.storeInSession(RFIDPalletSessionKeys.SESSION_SAVED_INVENTORY, tdoSave);
    }


}
