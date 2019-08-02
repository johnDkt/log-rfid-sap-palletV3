package com.decathlon.log.rfid.pallet.sgtin.impl;

import com.decathlon.log.rfid.epc.decoder.EPC;
import com.decathlon.log.rfid.epc.decoder.EPCDecoder;
import com.decathlon.log.rfid.pallet.sgtin.SgtinDecoder;

/**
 * @author z01scarp
 */
public class LogSgtinDecoder implements SgtinDecoder {

    @Override
    public Object getEPC(String sgtin) throws Exception {
        return EPCDecoder.decode(sgtin);
    }

    @Override
    public String getItem(Object epcValue) throws Exception {
        EPC epc = (EPC) epcValue;
        return epc.getItem();
    }

    @Override
    public String getEan13(Object epcValue) throws Exception {
        EPC epc = (EPC) epcValue;
        return epc.getEan13();
    }

    @Override
    public String getGtin14(Object epcValue) throws Exception {
        EPC epc = (EPC) epcValue;
        return epc.getGtin14();
    }


}
