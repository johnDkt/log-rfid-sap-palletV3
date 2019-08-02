package com.decathlon.log.rfid.pallet.tdo;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TdoItemTest {

    @Test
    public void comparatorTests(){
        TdoItem article1 = new TdoItem();
        article1.setQtyRead(5);
        article1.setQtyTheo(10);
        TdoItem article2 = new TdoItem();
        article2.setQtyRead(7);
        article2.setQtyTheo(10);

        TdoItem article3 = new TdoItem();
        article3.setQtyRead(2);
        article3.setQtyTheo(10);

        assertTrue(article1.compareTo(article2) == -1);
        assertTrue(article1.compareTo(article3) == 1);
        assertTrue(article2.compareTo(article3) == 1);
        assertTrue(article3.compareTo(article2) == -1);
        assertTrue(article3.compareTo(article3) == 0);

    }

}