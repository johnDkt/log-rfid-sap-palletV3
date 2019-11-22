package com.decathlon.log.rfid.pallet.service.controller;

import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import com.decathlon.log.rfid.pallet.resources.ResourceManager;
import com.decathlon.log.rfid.pallet.service.sap.SapService;
import com.decathlon.log.rfid.pallet.service.sap.impl.SimpleSapService;
import com.decathlon.log.rfid.pallet.service.sap.mapper.HuMapper;
import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.decathlon.log.rfid.pallet.utils.RFIDProperties;
import com.decathlon.log.rfid.sap.client.exception.SapClientException;
import com.decathlon.log.rfid.sap.client.exception.SapClientExceptionType;
import lombok.extern.log4j.Log4j;
import org.jdesktop.application.Task;

import java.util.List;

@Log4j
public class SendHuWithContentTask extends Task<Void, Void> {

    private final SapService sapService;
    private final HuMapper mapper;

    private final String uat;
    private final List<TdoItem> tags;
    private final String mastName;
    private final String warehouse;

    public SendHuWithContentTask(final String uat, final List<TdoItem> tags) throws SapClientException {
        super(RFIDPalletApp.getApplication());
        this.sapService = new SimpleSapService();
        this.mapper = new HuMapper();
        this.uat = uat;
        this.tags = tags;
        this.mastName = RFIDProperties.getValue(RFIDProperties.PROPERTIES.MAT);
        this.warehouse = RFIDProperties.getValue(RFIDProperties.PROPERTIES.SAP_WAREHOUSE);
    }

    @Override
    protected Void doInBackground() throws SapClientException {
        log.info(mastName + " - hu " + uat + " sending " + tags.size() + " tags ");
        sapService.sendHuWithContent(mapper.mapToPostHuIdent(warehouse, uat, mastName, tags));
        return null;
    }

    @Override
    protected void failed(final Throwable cause) {
        if (cause instanceof SapClientException) {
            final SapClientException sapClientException = (SapClientException) cause;
            log.error(sapClientException.getMessage(), sapClientException);
            displayErrorMessageAccordingToTheExceptionType(sapClientException);
        } else {
            log.error("Unknown error while communicate with the server", cause);
            redirectToParamPanelWithErrorMessage(ResourceManager.getInstance().getString("ParamPanel.error.server.standard"));
        }
    }

    @Override
    protected void succeeded(final Void result) {
        RFIDPalletApp.getView().showParamPanelAndClearScanTextField();
    }

    private void displayErrorMessageAccordingToTheExceptionType(final SapClientException sapException) {
        final SapClientExceptionType type = sapException.getType();
        String errorMessage = ResourceManager.getInstance().getString("ParamPanel.error.server.standard");
        if (SapClientExceptionType.TIMEOUT.equals(type)) {
            errorMessage = ResourceManager.getInstance().getString("ParamPanel.error.server.timeout");
        } else if (SapClientExceptionType.NOT_FOUND.equals(type)) {
            errorMessage = ResourceManager.getInstance().getString("ParamPanel.error.server.url.not.found");
        }
        redirectToParamPanelWithErrorMessage(errorMessage);
    }

    private void redirectToParamPanelWithErrorMessage(final String errorMessage) {
        RFIDPalletApp.getView().showParamPanel();
        RFIDPalletApp.getView().getParamPanel().setError(errorMessage);
    }
}
