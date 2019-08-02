package com.decathlon.log.rfid.pallet.service;

import com.decathlon.log.rfid.pallet.constants.Constants;
import com.decathlon.log.rfid.pallet.repository.BoExpedition;
import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.decathlon.log.rfid.pallet.tdo.TdoParameters;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;

public class PalletContentWebService {

    private static Logger LOGGER = Logger.getLogger(PalletContentWebService.class);

    private BoExpedition boExpedition;

    public PalletContentWebService() {
        boExpedition = new BoExpedition();
    }

    public List<TdoItem> getPalletContentFromWs(final TdoParameters parameters) throws Exception {

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

        final List<TdoItem> itemsList = getListOfTdoItem(mode, searchId);
        LOGGER.info("Received response from AS400 : " + ((itemsList.size() >= 0) ? itemsList.size() : "no") + " items found for " + searchId);
        return itemsList;
    }

    private List<TdoItem> getListOfTdoItem(final int mode, final String searchId) throws Exception {
        if (mode == Constants.MODE_UAT) {
            LOGGER.info("Search a pallet with this number : " + searchId);
            return boExpedition.convertToItemList(boExpedition.getPalletContentFromWs(searchId));
        } else {
            LOGGER.info("Search a parcel with this number : " + searchId);
            return boExpedition.convertToItemList(boExpedition.getColisContentFromWs(searchId));
        }
    }
}
