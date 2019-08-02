package com.decathlon.log.rfid.pallet.service.task;

import com.decathlon.log.rfid.pallet.constants.Constants;
import com.decathlon.log.rfid.pallet.repository.BoExpedition;
import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.decathlon.log.rfid.pallet.tdo.TdoParameters;
import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import com.decathlon.log.rfid.pallet.main.RFIDPalletSessionKeys;
import com.decathlon.log.rfid.pallet.service.SessionService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdesktop.application.Task;

import java.util.List;


/**
 * @author agodin16
 */
public class GetPalletContentWsTask extends Task<Object, Void> {

    private static Logger LOGGER = Logger.getLogger(GetPalletContentWsTask.class);

    private SessionService sessionService;
    private TdoParameters parameters;

    public GetPalletContentWsTask() {
        super(RFIDPalletApp.getApplication());
        this.sessionService = SessionService.getInstance();
        this.parameters = sessionService.retrieveFromSession(RFIDPalletSessionKeys.SESSION_PARAMETERS_KEY, TdoParameters.class);
    }


    @Override
    protected Object doInBackground() throws Exception {
        BoExpedition b = new BoExpedition();
        List<TdoItem> itemsList = null;

        String searchId = parameters.getSearchId();
        Logger.getLogger("action").info(parameters.getSearchId() + "|SEARCH");
        int mode = Constants.MODE_UAT;

        if (searchId.length() < Constants.NUM_UAT_LENGTH) {
            searchId = StringUtils.leftPad(searchId, Constants.NUM_UAT_LENGTH, '0');
        } else if (searchId.length() == Constants.NUM_UAT_LENGTH + 1) {
            searchId = searchId.substring(0, Constants.NUM_UAT_LENGTH);
        } else if (searchId.length() == Constants.NUM_COLIS_LENGTH) {
            mode = Constants.MODE_COLIS;
        }

        if (mode == Constants.MODE_UAT) {
            LOGGER.info("Search a pallet with this number : " + searchId);
            itemsList = b.convertToItemList(b.getPalletContentFromWs(searchId));
        } else if (mode == Constants.MODE_COLIS) {
            LOGGER.info("Search a colis with this number : " + searchId);
            itemsList = b.convertToItemList(b.getColisContentFromWs(searchId));
        }
        LOGGER.info("Recieved reponse from AS400 : " + ((itemsList.size() >= 0) ? itemsList.size() : "no") + " items found for " + searchId);
        sessionService.storeInSession(RFIDPalletSessionKeys.LISTE_ARTICLES_THEORIQUE, itemsList);
        return itemsList;
    }

    /**
     * @see org.jdesktop.application.Task#succeeded(java.lang.Object)
     */
    @Override
    protected void succeeded(Object result) {
        super.succeeded(result);
//		RFIDPalletApp.getView().initScanPanelLightWS();
    }

    /**
     * @see org.jdesktop.application.Task#failed(java.lang.Throwable)
     */
    @Override
    protected void failed(Throwable cause) {
        super.failed(cause);
    }
}
