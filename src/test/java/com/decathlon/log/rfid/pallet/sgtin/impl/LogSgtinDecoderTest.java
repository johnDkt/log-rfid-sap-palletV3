package com.decathlon.log.rfid.pallet.sgtin.impl;

import com.decathlon.log.rfid.epc.decoder.EPC;
import org.apache.log4j.Logger;
import org.junit.Test;


public class LogSgtinDecoderTest {
    private static Logger logger = Logger.getLogger(LogSgtinDecoderTest.class);

    @Test
    public void testLogSGTINDecoder() throws Exception {
        LogSgtinDecoder decoder = new LogSgtinDecoder();
        EPC epc = (EPC) decoder.getEPC("30395DFA812367800000082B");
        logger.info(epc.getItem());
        logger.info(epc.getEan13());
        logger.info(epc.getEan13().substring(2,9));
        logger.info(epc.getGtin14());


         epc = (EPC) decoder.getEPC("3038CD2482068E0000000001");
        logger.info(epc.getItem());
        logger.info(epc.getEan13());
        logger.info(epc.getEan13().substring(2,9));
        logger.info(epc.getGtin14());

    }


}