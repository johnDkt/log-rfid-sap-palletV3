package com.decathlon.log.rfid.pallet.ui.scan;

import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class ItemForTableDataMapper {

    public List<ItemForTableData> map(final List<TdoItem> items) {
        final List<ItemForTableData> result = new ArrayList<ItemForTableData>();

        for (final TdoItem item : items) {
            result.add(map(item));
        }

        return result;
    }

    private ItemForTableData map(final TdoItem item) {
        final List<String> eans = Lists.newArrayList(item.getEan());
        eans.addAll(item.getEncodedEan());

        return new ItemForTableData(item.getItem(), eans, item.getQtyTheo(),
                item.getFamily() + " " + item.getModel() + " " + item.getSize());
    }

}
