package com.decathlon.log.rfid.pallet.service.sap;

import com.decathlon.log.rfid.pallet.service.sap.config.SapConfigurationHolder;
import com.decathlon.log.rfid.sap.client.auth.SapCredential;
import com.decathlon.log.rfid.sap.client.client.SapConsumer;
import com.decathlon.log.rfid.sap.client.client.SapSimpleClient;
import com.decathlon.log.rfid.sap.client.client.config.ODataFormat;
import com.decathlon.log.rfid.sap.client.client.config.SapHttpConfig;
import com.decathlon.log.rfid.sap.client.exception.SapClientException;
import com.decathlon.log.rfid.sap.client.uri.ODataUriBuilder;
import com.decathlon.log.rfid.sap.client.uri.ODataUriKeys;

public class RetrieveHuSapConsumer {

    private static final String ROOT_FOR_RETRIEVE_SERVICE = "/Z_LOG_RFID_HU_CONTENT_SRV"; // /ZLOG_MOB_ODATA_SRV  | /Z_LOG_RFID_HU_CONTENT_SRV

    private final SapConsumer sapConsumer;
    private final SapConfigurationHolder sapConfig;
    private final ODataFormat format;

    public RetrieveHuSapConsumer(final SapConfigurationHolder sapConfig, final ODataFormat format) throws SapClientException {
        final String customServiceRoot = sapConfig.getServiceRoot() + ROOT_FOR_RETRIEVE_SERVICE;
        this.sapConfig = sapConfig;
        this.format = format;
        final String client = (sapConfig.getClient() != null) ? sapConfig.getClient() : SapHttpConfig.DEFAULT_CLIENT;
        final String lang = (sapConfig.getLang() != null) ? sapConfig.getLang() : SapHttpConfig.DEFAULT_LANG;
        this.sapConsumer = new SapSimpleClient(customServiceRoot, new SapCredential(sapConfig.getLogin(), sapConfig.getPassword()), new SapHttpConfig(format, sapConfig.getTimeout(), client, lang));
    }

    public <T> T readHu(final String hu, final Class<T> clazz) throws SapClientException {
        return sapConsumer.readEntitySet(buildUriForRetrieveHUItem(hu), clazz);
    }

    private String buildUriForRetrieveHUItem(final String searchedId) {
        final String entitySetName = "HUIDENTSet";
        final String warehouseKey = "Lgnum";
        final String huidentKey = "Huident";
        final String workcenterKey = "Workcenter";
        final String expandName = "ToHuContent";

        return new ODataUriBuilder(format)
                .withSegment(entitySetName,
                        ODataUriKeys.create(warehouseKey, sapConfig.getWarehouse()),
                        ODataUriKeys.create(huidentKey, searchedId),
                        ODataUriKeys.create(workcenterKey, ""))
                .expand(expandName)
                .build();
    }
}
