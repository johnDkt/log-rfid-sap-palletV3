package com.decathlon.log.rfid.pallet.service.sap.entity.post;

import com.decathlon.log.rfid.sap.client.annotation.SapEntity;
import com.decathlon.log.rfid.sap.client.annotation.SapEntitySet;
import com.decathlon.log.rfid.sap.client.annotation.SapProperty;
import lombok.Data;

@Data
@SapEntity(name = "HUCONTENT")
@SapEntitySet(name = "HUCONTENTSet")
public class PostHuContent {

    @SapProperty(name = "Lgnum")
    private String warehouse;

    @SapProperty(name = "Huident")
    private String hu;

    @SapProperty(name = "Epc")
    private String epc;

    @SapProperty(name = "Ean")
    private String ean;

    @SapProperty(name = "Matnr")
    private String itemCode;

    public static PostHuContent create(final String hu, final String warehouse, final String epc, final String ean,
                                       final String itemCode) {
        final PostHuContent huContent = new PostHuContent();
        huContent.setWarehouse(warehouse);
        huContent.setHu(hu);
        huContent.setEan(ean);
        huContent.setEpc(epc);
        huContent.setItemCode(itemCode);
        return huContent;
    }

}
