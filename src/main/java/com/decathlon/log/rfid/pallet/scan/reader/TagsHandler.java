package com.decathlon.log.rfid.pallet.scan.reader;

import com.decathlon.log.rfid.pallet.tdo.TdoItem;

public abstract class TagsHandler {

    public void handleTagRead(final TdoItem tdoItem) {
        updateUIWhenTagRead(tdoItem);
    }

    public abstract void updateUIWhenTagRead(final TdoItem tdoItem);
}
