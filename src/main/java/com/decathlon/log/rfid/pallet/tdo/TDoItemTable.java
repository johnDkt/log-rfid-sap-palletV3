package com.decathlon.log.rfid.pallet.tdo;

import lombok.Data;

@Data
public class TDoItemTable {

    private Object epc;
    private String sgtin;
    private String ean;
    private String gtin14;

}
