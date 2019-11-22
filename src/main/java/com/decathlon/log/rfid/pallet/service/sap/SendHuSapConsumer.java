package com.decathlon.log.rfid.pallet.service.sap;

import com.decathlon.log.rfid.pallet.service.sap.config.SapConfigurationHolder;
import com.decathlon.log.rfid.sap.client.auth.SapCredential;
import com.decathlon.log.rfid.sap.client.client.SapConsumer;
import com.decathlon.log.rfid.sap.client.client.SapSimpleClient;
import com.decathlon.log.rfid.sap.client.client.config.ODataFormat;
import com.decathlon.log.rfid.sap.client.client.config.SapHttpConfig;
import com.decathlon.log.rfid.sap.client.exception.SapClientException;
import com.decathlon.log.rfid.sap.client.uri.ODataUriBuilder;

public class SendHuSapConsumer {

    private static final String ROOT_FOR_RETRIEVE_SERVICE = "/ZLOG_MOB_ODATA_SRV" ; // not set this const if serviceRoot is ever set in application.properties. new value : Z_LOG_RFID_HU_CONTENT_SRV | old value : /ZLOG_MOB_ODATA_SRV"
    private SapConsumer sapConsumer = null;

    public SendHuSapConsumer(final SapConfigurationHolder sapConfig, final ODataFormat format) throws SapClientException {
        final String customServiceRoot = sapConfig.getServiceRoot() + ROOT_FOR_RETRIEVE_SERVICE;

        final String client = (sapConfig.getClient() != null) ? sapConfig.getClient() : SapHttpConfig.DEFAULT_CLIENT;
        final String lang = (sapConfig.getLang() != null) ? sapConfig.getLang() : SapHttpConfig.DEFAULT_LANG;
        this.sapConsumer = new SapSimpleClient(customServiceRoot, new SapCredential(sapConfig.getLogin(), sapConfig.getPassword()),
                    new SapHttpConfig(format, sapConfig.getTimeout(), client, lang));

    }

    public <T> void sendHu(final T entity) throws SapClientException {
        sapConsumer.writeEntitySet(buildUriForSendHu(), entity);
    }

    private String buildUriForSendHu() {
        return new ODataUriBuilder()
                .withSegment("HUIDENTSet")
                .build();
    }
}
