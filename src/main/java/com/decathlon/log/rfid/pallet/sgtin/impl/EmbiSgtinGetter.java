package com.decathlon.log.rfid.pallet.sgtin.impl;

import EmbiReader.SGTINBean;
import com.decathlon.log.rfid.pallet.sgtin.SgtinGetter;

public class EmbiSgtinGetter implements SgtinGetter {

    @Override
    public String getSgtinId(Object sgtinObject) {
        return mapToSGTINBean(sgtinObject).getSgtin();
    }

    @Override
    public String getAntenna(Object sgtinObject) {
        return mapToSGTINBean(sgtinObject).getAntenna();
    }

    private SGTINBean mapToSGTINBean(final Object object) {
        return SGTINBean.class.cast(object);
    }

}
