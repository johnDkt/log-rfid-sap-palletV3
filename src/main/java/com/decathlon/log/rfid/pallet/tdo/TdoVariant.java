package com.decathlon.log.rfid.pallet.tdo;


/**
 * @author z01scarp
 */
public class TdoVariant {

    private String variantCode;
    private String gridValue;
    private String ean13Code;
    private String family;
    private String par;
    private String stockKind;
    private String itemVar;
    private String numElem;
    private String numLevel;
    private String numOrg;
    private String model;


    public TdoVariant(String variantCode, String gridValue, String ean13Code,
                      String family) {
        super();
        setVariantCode(variantCode);
        this.gridValue = gridValue;
        this.ean13Code = ean13Code;
        this.family = family;

    }

    public String getVariantCode() {
        return variantCode;
    }

    public void setVariantCode(String variantCode) {
        this.variantCode = variantCode;
        for (int i = 1; variantCode != null && i <= 7 - variantCode.length(); i++) {
            this.variantCode = "0" + this.variantCode;
        }
    }

    public String getGridValue() {
        return gridValue;
    }

    public void setGridValue(String gridValue) {
        this.gridValue = gridValue;
    }

    public String getEan13Code() {
        return ean13Code;
    }

    public void setEan13Code(String ean13Code) {
        this.ean13Code = ean13Code;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPar() {
        return par;
    }

    public void setPar(String par) {
        this.par = par;
    }

    public String getStockKind() {
        return stockKind;
    }

    public void setStockKind(String stockKind) {
        this.stockKind = stockKind;
    }

    public String getItemVar() {
        return itemVar;
    }

    public void setItemVar(String itemVar) {
        this.itemVar = itemVar;
    }

    public String getNumElem() {
        return numElem;
    }

    public void setNumElem(String numElem) {
        this.numElem = numElem;
    }

    public String getNumLevel() {
        return numLevel;
    }

    public void setNumLevel(String numLevel) {
        this.numLevel = numLevel;
    }

    public String getNumOrg() {
        return numOrg;
    }

    public void setNumOrg(String numOrg) {
        this.numOrg = numOrg;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


}
