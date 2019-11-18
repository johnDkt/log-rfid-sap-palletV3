package com.decathlon.log.rfid.pallet.service.controller;

import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import com.decathlon.log.rfid.pallet.resources.ResourceManager;
import com.decathlon.log.rfid.pallet.service.sap.SapService;
import com.decathlon.log.rfid.pallet.service.sap.config.EWMErrorCode;
import com.decathlon.log.rfid.pallet.service.sap.impl.SimpleSapService;
import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.decathlon.log.rfid.sap.client.exception.SapClientException;
import com.decathlon.log.rfid.sap.client.exception.SapClientExceptionType;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.jdesktop.application.Task;

import java.util.List;

@Log4j
public class RetrieveHUItemFromTheServerTask extends Task<List<TdoItem>, Object> {

    private SapService sapService;

    private String searchId;

    public RetrieveHUItemFromTheServerTask(final String searchId) {
        super(RFIDPalletApp.getApplication());
        sapService = new SimpleSapService();
        this.searchId = searchId;
    }

    @Override
    protected List<TdoItem> doInBackground() throws SapClientException {
        Logger.getLogger("action").info(this.searchId + "|SEARCH");
        return sapService.retrieveContentFrom(this.searchId);
    }

    @Override
    protected void succeeded(List<TdoItem> result) { //TOdo cmt il arrive � recup une list de TdoItem
        RFIDPalletApp.getView().getScanPanelLight().initUIWithDataFromServer(result);
        if (result.isEmpty()) {
            RFIDPalletApp.getView().getScanPanelLight().displayAdditionalMessage(
                    ResourceManager.getInstance().getString("ScanPanelLight.additional.information.no.ean")
            );
        }
    }

    @Override
    protected void failed(final Throwable cause) {
        if (cause instanceof SapClientException) {
            final SapClientException sapClientException = (SapClientException) cause;
            log.error(sapClientException.getMessage(), sapClientException);
            displayErrorMessageAccordingToTheExceptionType(sapClientException);
        } else {
            log.error("Unknown error while communicate with the server", cause);
            stopScannerAndShowParamPanel(ResourceManager.getInstance().getString("ParamPanel.error.server.standard"));
        }
    }

    private void displayErrorMessageAccordingToTheExceptionType(final SapClientException sapException) {
        final SapClientExceptionType type = sapException.getType();
        String errorMessage = ResourceManager.getInstance().getString("ParamPanel.error.server.standard");

        if (sapException.hasEWMError()) {
            errorMessage = chooseTheRightErrorMessage(sapException);
        } else if (SapClientExceptionType.NOT_FOUND.equals(type)) {
            errorMessage = ResourceManager.getInstance().getString("ParamPanel.error.server.url.not.found");
        } else if (SapClientExceptionType.TIMEOUT.equals(type)) {
            errorMessage = ResourceManager.getInstance().getString("ParamPanel.error.server.timeout");
        }
        stopScannerAndShowParamPanel(errorMessage);
    }

    private String chooseTheRightErrorMessage(final SapClientException sapException) {
        if (sapException.getErrorCode().isPresent()) {
            if (EWMErrorCode.HU_NOT_FOUND.getErrorCode().equals(sapException.getErrorCode().get())) {
                return ResourceManager.getInstance().getString("ParamPanel.error.server.hu.not.found");
            }
        }
        return ResourceManager.getInstance().getString("ParamPanel.error.server.standard");
    }

    private void stopScannerAndShowParamPanel(final String errorMessage) {
        RFIDPalletApp.getView().getScanPanelLight().canceledScanner();
        RFIDPalletApp.getView().showParamPanel();
        RFIDPalletApp.getView().getParamPanel().setError(errorMessage);
    }
}
