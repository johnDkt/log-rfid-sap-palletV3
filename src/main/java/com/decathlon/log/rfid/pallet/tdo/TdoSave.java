package com.decathlon.log.rfid.pallet.tdo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TdoSave {

    private Date date;
    private String truck;
    private List<TdoSgtin> sgtins;

    public TdoSave() {
        super();
        this.sgtins = new ArrayList<TdoSgtin>();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public List<TdoSgtin> getSgtins() {
        return sgtins;
    }

    public void setSgtins(List<TdoSgtin> sgtins) {
        this.sgtins = sgtins;
    }
}
