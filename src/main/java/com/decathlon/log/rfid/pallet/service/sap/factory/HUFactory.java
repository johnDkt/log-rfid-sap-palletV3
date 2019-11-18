package com.decathlon.log.rfid.pallet.service.sap.factory;

import com.decathlon.log.rfid.pallet.service.sap.entity.get.HUContent;
import com.decathlon.log.rfid.pallet.service.sap.entity.get.HuIdentSet;

import java.math.BigDecimal;
import java.util.List;

public class HUFactory {

    private HUFactory() {
        //EMPTY CONSTRUCTOR
    }

    public static HuIdentSet createHUIdent(final String warehouse, final String huIdent,
                                           final List<HUContent> children) {
        final HuIdentSet huIdentSet = new HuIdentSet();
        huIdentSet.setHuIdent(huIdent);
        huIdentSet.setLgNum(warehouse);
        huIdentSet.setHuContents(children);
        return huIdentSet;
    }


    public static HUContent createHuContent(final String warehouse, final String huIdent, final String itemCode,
                                            final String ean, final String desc, final String brand, final int qty,
                                            final byte[] batChId, final String unit) {
        final HUContent huContent = new HUContent();
        huContent.setLgnum(warehouse);
        huContent.setHuident(huIdent);
        huContent.setMatnr(itemCode);
        huContent.setEan11(ean);
        huContent.setMaktx(desc);
        huContent.setQuan(BigDecimal.valueOf(qty)); //BigDecimal.valueOf(qty)
        huContent.setUnit(unit);
        huContent.setBrand(brand);
        huContent.setBatchid(new String(String.valueOf(batChId))); //avant cétait un Byte directement ds la class hucontent
        return huContent;
    }

}
