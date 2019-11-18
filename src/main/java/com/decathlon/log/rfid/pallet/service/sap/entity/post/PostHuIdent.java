package com.decathlon.log.rfid.pallet.service.sap.entity.post;

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
public class PostHuIdent {

    @SapProperty(name = "Lgnum")
    private String warehouse;

    @SapProperty(name = "Huident")
    private String hu;

    @SapProperty(name = "MaterialReader")
    private String mastName;

    @SapNavigationProperty(name = "ToHuContent", clazz = PostHuContent.class)
    private List<PostHuContent> huContents;

    public static PostHuIdent create(final String warehouse, final String hu, final String mastName,
                                     final List<PostHuContent> huContents) {
        final PostHuIdent huIdent = new PostHuIdent();
        huIdent.setHu(hu);
        huIdent.setWarehouse(warehouse);
        huIdent.setMastName(mastName);
        huIdent.setHuContents(huContents);
        return huIdent;
    }

    public static PostHuIdent create(final String warehouse, final String hu, final String mastName) {
        return create(warehouse, hu, mastName, Lists.<PostHuContent>newArrayList());
    }

}
