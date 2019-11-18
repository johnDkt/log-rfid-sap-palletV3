package com.decathlon.log.rfid.pallet.service.sap.entity.get;

import com.decathlon.log.rfid.sap.client.annotation.SapEntity;
import com.decathlon.log.rfid.sap.client.annotation.SapEntitySet;
import com.decathlon.log.rfid.sap.client.annotation.SapNavigationProperty;
import com.decathlon.log.rfid.sap.client.annotation.SapProperty;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
@SapEntity(name = "HUIDENT")
@SapEntitySet(name = "HUIDENTSet")
public class HuIdentSet {

    @SapProperty(name = "Lgnum")
    private String lgNum;

    @SapProperty(name = "Huident")
    private String huIdent;

    @SapNavigationProperty(name = "ToHuContent", clazz = HUContent.class)
    private List<HUContent> huContents = Lists.newArrayList();

}
