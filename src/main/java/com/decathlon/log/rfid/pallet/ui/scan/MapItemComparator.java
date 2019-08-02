package com.decathlon.log.rfid.pallet.ui.scan;

import com.decathlon.log.rfid.pallet.tdo.TdoItem;

import java.util.Comparator;
import java.util.Map;

public class MapItemComparator implements Comparator<Object> {
    Map<String, TdoItem> map;

    public MapItemComparator(Map<String, TdoItem> map) {
        this.map = map;
    }

    public int compare(Object o1, Object o2) {
        TdoItem item1 =map.get(o1);
        TdoItem item2 =map.get(o2);

        if(item1 == null || item2 == null){
            return -1;
        }
        return item1.compareTo(item2);

    }
}
