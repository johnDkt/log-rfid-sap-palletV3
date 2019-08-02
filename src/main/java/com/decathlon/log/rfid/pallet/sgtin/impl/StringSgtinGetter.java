package com.decathlon.log.rfid.pallet.sgtin.impl;

import com.decathlon.log.rfid.pallet.sgtin.SgtinGetter;

public class StringSgtinGetter implements SgtinGetter {

    @Override
    public String getSgtinId(Object sgtinObject) {
        return sgtinObject.toString();
    }

    @Override
    public String getAntenna(Object sgtinObject) {
        return null;
    }

}
