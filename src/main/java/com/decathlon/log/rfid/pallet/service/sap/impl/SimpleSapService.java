package com.decathlon.log.rfid.pallet.service.sap.impl;

import com.decathlon.log.rfid.pallet.manager.DoItemManager;
import com.decathlon.log.rfid.pallet.service.sap.RetrieveHuSapConsumer;
import com.decathlon.log.rfid.pallet.service.sap.SapService;
import com.decathlon.log.rfid.pallet.service.sap.SendHuSapConsumer;
import com.decathlon.log.rfid.pallet.service.sap.config.SapConfigurationHolder;
import com.decathlon.log.rfid.pallet.service.sap.entity.get.HuIdentSet;
import com.decathlon.log.rfid.pallet.service.sap.mapper.TdoItemMapper;
import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.decathlon.log.rfid.sap.client.client.config.ODataFormat;
import com.decathlon.log.rfid.sap.client.exception.SapClientException;
import lombok.extern.log4j.Log4j;

import java.util.List;

@Log4j
public class SimpleSapService implements SapService {

    private final TdoItemMapper tdoItemMapper;
    private final DoItemManager doItemManager;

    private final RetrieveHuSapConsumer retrieveHuSapConsumer;
    private final SendHuSapConsumer sendHuSapConsumer;

    private final SapConfigurationHolder sapConfig;

    public SimpleSapService() {
        tdoItemMapper = new TdoItemMapper();
        doItemManager = new DoItemManager();

        sapConfig = SapConfigurationHolder.getInstance();

        final ODataFormat format = ODataFormat.ATOM;
        retrieveHuSapConsumer = new RetrieveHuSapConsumer(sapConfig, format);
        sendHuSapConsumer = new SendHuSapConsumer(sapConfig, format);

        log.trace("SAP consumer is initialized !");
    }

    @Override
    public List<TdoItem> retrieveContentFrom(final String searchId) throws SapClientException {
        log.info("Call : retrieveContentFrom with searchId : " + searchId);
        final HuIdentSet result = retrieveHuSapConsumer.readHu(searchId, HuIdentSet.class);
        log.info("Huident - " + result.getLgNum() + " - " + result.getHuIdent() + " has " + result.getHuContents().size() + " items");
        return doItemManager.gatherDoItemByEan(tdoItemMapper.mapTo(result));
    }

    @Override
    public <T> void sendHuWithContent(final T entityToSave) throws SapClientException {
        log.info("Call : sending Hu");
        sendHuSapConsumer.sendHu(entityToSave);
        log.info("Hu sent");
    }

}