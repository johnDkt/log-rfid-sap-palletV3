package com.decathlon.log.rfid.pallet.ui.scan;

import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import lombok.Getter;

@Getter
public class ItemReadForTableData {

    private final String epc;
    private final String ean;
    private final String itemCode;

    public ItemReadForTableData(final String ean, final String epc, final String itemCode) {
        this.ean = ean;
        this.epc = epc;
        this.itemCode = itemCode;
    }

    public ItemReadForTableData(final TdoItem item) {
        this(item.getEan(), item.getSgtin(), item.getItem());
    }

}
