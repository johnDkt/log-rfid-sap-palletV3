package com.decathlon.log.rfid.pallet.service.sap.entity.get;

import com.decathlon.log.rfid.sap.client.annotation.SapEntity;
import com.decathlon.log.rfid.sap.client.annotation.SapEntitySet;
import com.decathlon.log.rfid.sap.client.annotation.SapProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@SapEntity(name = "HUCONTENT")
@SapEntitySet(name = "HUCONTENTSet")
public class HUContent {

    @SapProperty(name = "Inspdocno")
    private String inspdocno;

    @SapProperty(name = "Zzrfid")
    private String zzrfid;

    @SapProperty(name = "Lgnum")
    private String lgnum;

    @SapProperty(name = "Huident")
    private String huident ;

    @SapProperty(name = "Inspidtyp")
    private String inspidtyp;

    @SapProperty(name = "DcodBundId")
    private String dcodBundId;

    @SapProperty(name = "Matnr")
    private String matnr;

    @SapProperty(name = "DeciCodeId")
    private String deciCodeId;

    @SapProperty(name = "Ean11")
    private String ean11;

    @SapProperty(name = "Batchid")
    private String batchid;

    @SapProperty(name = "FindTypeId")
    private String findTypeId;

    @SapProperty(name = "CatFiltId")
    private String catFiltId;

    @SapProperty(name = "Quan")
    private BigDecimal quan;

    @SapProperty(name = "Defgrp")
    private String defgrp;

    @SapProperty(name = "Unit")
    private String unit;

    @SapProperty(name = "Defect")
    private String defect;

    @SapProperty(name = "Maktx")
    private String maktx;

    @SapProperty(name = "Brand")
    private String brand;

    @SapProperty(name = "Workcenter")
    private String workcenter;

}
