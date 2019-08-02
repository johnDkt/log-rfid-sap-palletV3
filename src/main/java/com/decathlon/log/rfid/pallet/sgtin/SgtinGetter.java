package com.decathlon.log.rfid.pallet.sgtin;

/**
 * @author z01scarp
 */
public interface SgtinGetter {

    String getSgtinId(Object sgtinObject);

    String getAntenna(Object sgtinObject);
}
