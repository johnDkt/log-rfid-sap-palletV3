package com.decathlon.log.rfid.pallet.service;


import com.decathlon.log.rfid.pallet.sgtin.SgtinDecoder;
import com.decathlon.log.rfid.pallet.sgtin.SgtinGetter;
import com.decathlon.log.rfid.pallet.sgtin.impl.EmbiSgtinGetter;
import com.decathlon.log.rfid.pallet.sgtin.impl.LogSgtinDecoder;
import com.decathlon.log.rfid.pallet.sgtin.impl.StringSgtinGetter;
import com.decathlon.log.rfid.pallet.utils.RFIDProperties;
import lombok.extern.log4j.Log4j;

@Log4j
public class SgtinService {

    private static SgtinService instance;
    private SgtinGetter sgtinGetter;
    private SgtinDecoder sgtinDecoder;

    private SgtinService() {
    }

    public static SgtinService getInstance() {
        synchronized (SgtinService.class) {
            if (instance == null) {
                instance = new SgtinService();
            }
            return instance;
        }
    }

    public void initSgtinDecoderAndGetter() throws Exception {
        final String readerType = RFIDProperties.getValue(RFIDProperties.PROPERTIES.READER_TYPE);
        log.debug("Reader type : " + readerType);
        if (readerType != null) {
            if (READER_TYPE.EMBI.toString().equals(readerType)) {
                sgtinGetter = new EmbiSgtinGetter();
            } else if ((READER_TYPE.STRING.toString().equals(readerType))) {
                sgtinGetter = new StringSgtinGetter();
            } else {
                throw new Exception("READER TYPE must be equal to EMBI or STRING");
            }
        } else {
            throw new Exception("READER TYPE must not be null but equal to EMBI or STRING");
        }

        final String decoderType = RFIDProperties.getValue(RFIDProperties.PROPERTIES.SGTIN_DECODER_TYPE);
        if (decoderType != null) {
            if (DECODER_TYPE.LOG.toString().equals(decoderType)) {
                sgtinDecoder = new LogSgtinDecoder();
            } else {
                throw new Exception("DECODER TYPE must be equal to LOG");
            }
        } else {
            throw new Exception("DECODER TYPE must not be null but equal to LOG");
        }
    }

    public SgtinGetter getSgtinGetter() {
        return this.sgtinGetter;
    }

    public SgtinDecoder getSgtinDecoder() {
        return this.sgtinDecoder;
    }

    private enum READER_TYPE {
        EMBI("EMBI"),
        STRING("STRING");

        private String value;

        READER_TYPE(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    private enum DECODER_TYPE {
        LOG("LOG");

        private String value;

        DECODER_TYPE(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

}
