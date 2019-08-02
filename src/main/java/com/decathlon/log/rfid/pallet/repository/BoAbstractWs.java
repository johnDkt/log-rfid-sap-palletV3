package com.decathlon.log.rfid.pallet.repository;

import com.decathlon.log.rfid.pallet.utils.RFIDProperties;
import com.decathlon.log.rfid.pallet.utils.RFIDProperties.PROPERTIES;
import org.apache.axis2.AxisFault;

import javax.xml.namespace.QName;

public abstract class BoAbstractWs {

    public static final String WS_PARCEL_DETAILS_NAME = "WsParcelDetails";
    protected static String library;
    private static String baseUrl;
    private static Long timeout;
    private static BoConnectionLogger log;
    private String wsName;
    private String url;

    protected BoAbstractWs(final String wsName) {
        this.wsName = wsName;
        if (baseUrl == null) {
            baseUrl = RFIDProperties.getValue(PROPERTIES.WS_URL).trim();
        }

        if (timeout == null) {
            timeout = Long.parseLong(RFIDProperties.getValue(PROPERTIES.WS_CALL_TIMEOUT).trim());
        }

        if (library == null) {
            library = RFIDProperties.getValue(PROPERTIES.AS400_LIBRARY).trim();
        }
        if (log == null) {
            log = new BoConnectionLogger();
        }
        url = baseUrl + this.wsName;
    }

    protected String getUrl() {
        return this.url;
    }

    protected void init(final org.apache.axis2.client.Stub stub) throws AxisFault {
        stub._getServiceClient().addStringHeader(new QName("com.decathlon.fwk", "DKT_SSL_CLIENT_UID"), "log-rfid-pallet");
        stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(timeout);
    }

    protected void log(final String wsMethod, final long duration) {
        log.log(this.wsName, wsMethod, duration);
    }

}
