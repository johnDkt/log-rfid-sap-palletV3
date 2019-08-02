package com.decathlon.log.rfid.pallet.sgtin;

public interface SgtinDecoder {

    Object getEPC(String sgtin) throws Exception;

    String getItem(Object epc) throws Exception;

    String getGtin14(Object epc) throws Exception;

    String getEan13(Object epc) throws Exception;
}
