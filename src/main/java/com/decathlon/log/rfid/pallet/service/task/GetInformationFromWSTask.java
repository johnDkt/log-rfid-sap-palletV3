package com.decathlon.log.rfid.pallet.service.task;

import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.decathlon.log.rfid.pallet.tdo.TdoParameters;
import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import com.decathlon.log.rfid.pallet.main.RFIDPalletSessionKeys;
import com.decathlon.log.rfid.pallet.service.PalletContentWebService;
import com.decathlon.log.rfid.pallet.service.SessionService;
import org.jdesktop.application.Task;

import java.util.List;

public class GetInformationFromWSTask extends Task<Object, Void> {

    private PalletContentWebService palletContentWebService;
    private SessionService sessionService;

    public GetInformationFromWSTask() {
        super(RFIDPalletApp.getApplication());
        palletContentWebService = new PalletContentWebService();
        sessionService = SessionService.getInstance();
    }


    @Override
    protected List<TdoItem> doInBackground() throws Exception {
        return palletContentWebService.getPalletContentFromWs(
                sessionService.retrieveFromSession(RFIDPalletSessionKeys.SESSION_PARAMETERS_KEY, TdoParameters.class));
    }

    @Override
    protected void succeeded(final Object result) {
        super.succeeded(result);
        sessionService.storeInSession(RFIDPalletSessionKeys.LISTE_ARTICLES_THEORIQUE, result);
    }

}
