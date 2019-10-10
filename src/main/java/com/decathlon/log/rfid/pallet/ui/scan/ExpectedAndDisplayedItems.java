package com.decathlon.log.rfid.pallet.ui.scan;

import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Slf4j
public class ExpectedAndDisplayedItems {

    private List<ItemForTableData> items;
    private ItemForTableData uselessItem;
    @Setter
    public boolean dataFromTheServer;

    public ExpectedAndDisplayedItems() {
        items = new ArrayList<ItemForTableData>();
        uselessItem = new ItemForTableData("Useless");
    }

    public void setTheoreticalData(final List<ItemForTableData> items) {
        this.items = items;
        dataFromTheServer = true;
        System.out.println("dataFromTheServer = true");
    }

    public void upUselessQuantity() {
        this.uselessItem.upQty();
    }

    public boolean addReadItem(final TdoItem tdoItem) {
        double startTime = System.nanoTime();
        Boolean epcMathing = false;
        if (!dataFromTheServer || items.isEmpty()) {
            upUselessQuantity();
            return false;
        }

        for(ItemForTableData item : items){
            if(item.hasEan(tdoItem.getEan()) || compareItemCode(tdoItem,item)){
                item.upQty();
                epcMathing = true;
            }
        }

        if(!epcMathing){
            upUselessQuantity();
        }

        Collections.sort(items);

        double endTime = System.nanoTime();
        double duration = (endTime - startTime);
        log.trace("matching algo execution time = "+duration/1000000+"ms");
        return epcMathing;
    }

    //evolution to handle Ean code started by 21 or 200
    public Boolean compareItemCode(TdoItem objRfidDataReaded , ItemForTableData objGe ){
        String ean = objRfidDataReaded.getEan() ;
        String rfidCodeArt = "";
        Boolean returnValue = false;
        int i = 0;
        if( null != ean && !ean.equals("")){ // constantes à externaliser
            if("21".equals(ean.substring(0,2))){
                rfidCodeArt = ean.substring(2,9);
            }else if("200".equals(ean.substring(0,3))){
                rfidCodeArt = ean.substring(3,9);
            }
            if(!rfidCodeArt.equals("") && rfidCodeArt.length() >= 6){
                returnValue = matchingWithEan(rfidCodeArt,objGe.getItemCode());
            }
        }
        return returnValue;
    }

    /**
     * Compare article itemCode with itemCode deducted from tag.
     * Delete all zero and for each zero suppressed test equality between deducted ItemCode and article itemCode.
     * @param itemCodeDeductedFromTag
     * @return
     */
    public static Boolean matchingWithEan(String itemCodeDeductedFromTag, String itemCodeOfProduct){
        int i = 0;
        while (i<=itemCodeDeductedFromTag.length()) {
            i++;
            if(itemCodeDeductedFromTag.equals(itemCodeOfProduct)){
                return true;
            }
            if(itemCodeDeductedFromTag.length() >= 1 && itemCodeDeductedFromTag.charAt(0) == '0'){
                itemCodeDeductedFromTag = itemCodeDeductedFromTag.substring(1); // delete zero
            }else{
                break;
            }
        }
        return false;
    }

    public int size() {
        return items.size() + 1;
    }

    public ItemForTableData get(final int index) {
        if (index == 0) {
            return uselessItem;
        }
        return items.get(index - 1);
    }

    public void clear() {
        uselessItem = new ItemForTableData("Useless");
        items.clear();
    }

    public boolean isEmpty() {
        return uselessItem.getQtyRead() == 0 &&
                items.isEmpty();
    }

    public boolean hasDataFromServer() {
        return dataFromTheServer;
    }

}
