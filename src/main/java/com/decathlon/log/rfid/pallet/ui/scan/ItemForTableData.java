package com.decathlon.log.rfid.pallet.ui.scan;

import com.google.common.collect.Lists;

import java.util.List;

public class ItemForTableData implements Comparable<ItemForTableData> {

    private final String description;
    private final int qtyExpected;
    private final String itemCode;
    private final List<String> eans;

    private int qtyRead;

    public ItemForTableData(final String itemCode, final List<String> eans, final int qtyExpected, final String description) {
        this.itemCode = itemCode;
        this.eans = eans;
        this.qtyExpected = qtyExpected;
        this.description = description;
        this.qtyRead = 0;
    }

    public ItemForTableData(final String description) {
        this("no item code", Lists.newArrayList("no ean"), 0, description);
    }

    @Override
    public String toString() {
        return "ItemForTableData{" +
                "description='" + description + '\'' +
                ", qtyExpected=" + qtyExpected +
                ", itemCode='" + itemCode + '\'' +
                ", eans=" + eans +
                ", qtyRead=" + qtyRead +
                '}';
    }

    public boolean hasEan(final String eanToFind) {
        if(null != eanToFind ){
            for (final String ean : eans) {
                if(null != ean && !("".equals(ean)) && ean.equals(eanToFind)){
                    return true;
                }
            }
        }
        return false;
    }

    public void upQty() {
        this.qtyRead++;
    }

    public void setQtyRead(int qtyRead) {
        this.qtyRead = qtyRead;
    }

    public String getDescription() {
        return this.description;
    }

    public int getQtyExpected() {
        return this.qtyExpected;
    }

    public String getItemCode() {
        return this.itemCode;
    }

    public List<String> getEans() {
        return this.eans;
    }

    public int getQtyRead() {
        return this.qtyRead;
    }

    @Override
    public int compareTo(ItemForTableData o) {

        // avoid division by zero
        if(o.getQtyExpected() == 0 || this.getQtyExpected() == 0){
            return 1;
        }

        // compute normal case
        int oPercentRatio = (o.getQtyRead()*100) / o.getQtyExpected();
        int thisPercentRatio = (this.getQtyRead()*100) / this.getQtyExpected();

        //high ratio at the bottom
        if (oPercentRatio > thisPercentRatio){
            return -1;
        }
        // low ratio at the top
        if(oPercentRatio < thisPercentRatio){
            return 1;
        }

        return 0;
    }
}
