package com.decathlon.log.rfid.pallet.service.sap.mapper;

import com.decathlon.connectJavaIntegrator.utils.Utils;
import com.decathlon.log.rfid.pallet.service.sap.entity.get.HUContent;
import com.decathlon.log.rfid.pallet.service.sap.entity.get.HuIdentSet;
import com.decathlon.log.rfid.pallet.tdo.TdoItem;

import java.util.ArrayList;
import java.util.List;

public class TdoItemMapper {

    public List<TdoItem> mapTo(final HuIdentSet huIdentSet) {
        final List<TdoItem> result = new ArrayList<TdoItem>();
        for (final HUContent huContent : huIdentSet.getHuContents()) {
            if (!huContent.getZzrfid().equals("N")) { //Si l'article scanné n'est pas de type "Non RFID"
                result.add(mapTo(huContent));
            }
        }
        return result;
    }

    public TdoItem mapTo(final HUContent huContent) {
        final TdoItem result = new TdoItem();
        result.setQtyTheo(huContent.getQuan().intValue());
        result.setEan(huContent.getEan11());
        result.setEncodedEan(Utils.transformEanFieldReceiveFromWsIntoList(huContent.getEan11()));
        result.setItem(huContent.getMatnr());
        result.setRawDescription(huContent.getBrand() + " - " + huContent.getMaktx());
        return result;
    }
}
