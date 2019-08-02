package com.decathlon.log.rfid.pallet.repository;

import lombok.extern.log4j.Log4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j
public class BoConnectionLogger {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");

    public void log(String wsName, String method, long duration) {
        log.info(wsName + "-" + method + ";" + sdf.format(new Date()) + ";" + duration);
    }


}