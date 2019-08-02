package com.decathlon.log.rfid.pallet.tdo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TdoItem implements Comparable<TdoItem>{

    private String sgtin;
    private String ean;
    private String gtin14;
    private String family;
    private String model;
    private String item;
    private String encodedItem;
    private String size;
    private int qtyRead;
    private int qtyTheo;
    private List<String> encodedEan;

    public TdoItem() {
        this("","","","");
    }

    public TdoItem(String item, String size, String ean, String family) {
        this.item = item;
        this.size = size;
        this.ean = ean;
        this.family = family;
        this.model = "";
        encodedEan = new ArrayList<String>();
    }

    public TdoItem(final String item, final String ean) {
        this(item, "", ean, "");
    }

    public void addEncodedEan(final String encodedEan) {
        this.encodedEan.add(encodedEan);
    }

    @Override
    public int compareTo(TdoItem o) {
        if (o.getReadPercent() > this.getReadPercent()){
            return -1;
        }
        if (o.getReadPercent() < this.getReadPercent()){
            return 1;
        }
        if (o.getQtyTheo() > this.getQtyTheo()){
            return -1;
        }
        if (o.getQtyTheo() < this.getQtyTheo()){
            return 1;
        }
        return  this.getEan().compareTo(o.getEan());
    }

    public double getReadPercent(){
        if(qtyTheo == 0){
            return -1;
        }
        return ((double)qtyRead / (double)qtyTheo)*100;
    }

    public void incrementReadQuantity() {
        this.qtyRead +=1;
    }
}
