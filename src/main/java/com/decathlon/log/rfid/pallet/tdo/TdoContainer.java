package com.decathlon.log.rfid.pallet.tdo;


/**
 * @author z01scarp
 */
public class TdoContainer {


    public static final TdoContainer EMPTY_CONTAINER = new TdoContainer("", 0, false, new TdoVariant(null, null, null, null));
    private String name;
    private Integer initTheoricalQty;
    private Boolean rfid;
    private Boolean open;
    private Boolean pickingInProgress;
    private TdoVariant variant;
    private int theoricalQty;
    private int readQty;
    private int recommendedTimeout = -1;
    private boolean hasAlreadyBeenMoved = false;
    private int index = -1;


    public TdoContainer(String name, Integer initTheoricalQty, Boolean rfid, TdoVariant variant) {
        super();
        this.setName(name);
        this.initTheoricalQty = initTheoricalQty;
        this.rfid = rfid;
        this.variant = variant;
        this.readQty = 0;
    }

    public Integer getInitTheoricalQty() {
        return initTheoricalQty;
    }

    public void setInitTheoricalQty(Integer initTheoricalQty) {
        this.initTheoricalQty = initTheoricalQty;
    }

    public Boolean isRfid() {
        return rfid;
    }

    public void setRfid(Boolean rfid) {
        this.rfid = rfid;
    }

    public TdoVariant getVariant() {
        return variant;
    }

    public void setVariant(TdoVariant variant) {
        this.variant = variant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        for (int i = 1; i <= 6 - name.length(); i++) {
            this.name = "0" + this.name;
        }
    }


    public int getTheoricalQty() {
        return theoricalQty;
    }

    public void setTheoricalQty(int theoricalQty) {
        this.theoricalQty = theoricalQty;
    }

    public boolean isEmpty() {
        return this.getName().isEmpty();
    }

    public boolean hasAlreadyBeenMoved() {
        return hasAlreadyBeenMoved;
    }

    public void move(boolean move) {
        this.hasAlreadyBeenMoved = move;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public int getRecommendedTimeout() {
        return recommendedTimeout;
    }

    public void setRecommendedTimeout(int recommendedTimeout) {
        this.recommendedTimeout = recommendedTimeout;
    }

    public Boolean getPickingInProgress() {
        return pickingInProgress;
    }

    public void setPickingInProgress(Boolean pickingInProgress) {
        this.pickingInProgress = pickingInProgress;
    }

    public int getReadQty() {
        return readQty;
    }

    public void setReadQty(int readQty) {
        this.readQty = readQty;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
