package com.decathlon.log.rfid.pallet.service;

import com.decathlon.log.rfid.pallet.scan.reader.TagsListener;
import com.decathlon.log.rfid.pallet.utils.RFIDProperties;
import com.decathlon.log.rfid.reader.bo.BoScanner;
import com.decathlon.log.rfid.reader.bo.ScannerFactory;
import lombok.Getter;

public class ScannerService {

    private static ScannerService instance;

    @Getter
    private BoScanner boScanner;

    @Getter
    private TagsListener tagsListener;

    private ScannerService() {}

    public static ScannerService getInstance() {
        synchronized (ScannerService.class) {
            if (instance == null) {
                instance = new ScannerService();
            }
            return instance;
        }
    }

    public void initScanner() throws Exception {
        this.boScanner = ScannerFactory.getScanner(RFIDProperties.getValue(RFIDProperties.PROPERTIES.CONNECTION_TYPE),
                RFIDProperties.getValue(RFIDProperties.PROPERTIES.DEVICE_TYPE));
    }

    public void setTagsListener(final TagsListener tagsListener) {
        this.tagsListener = tagsListener;
        this.tagsListener.setScanner(boScanner);
        this.boScanner.addPropertyChangeListener(tagsListener);
    }

}
