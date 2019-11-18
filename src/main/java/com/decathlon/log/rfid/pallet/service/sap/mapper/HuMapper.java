package com.decathlon.log.rfid.pallet.service.sap.mapper;

import com.decathlon.log.rfid.pallet.service.sap.entity.post.PostHuContent;
import com.decathlon.log.rfid.pallet.service.sap.entity.post.PostHuIdent;
import com.decathlon.log.rfid.pallet.tdo.TdoItem;

import java.util.List;

public class HuMapper {

    public PostHuIdent mapToPostHuIdent(final String warehouse, final String hu, final String mastName,
                                        final List<TdoItem> items) {
        final PostHuIdent result = PostHuIdent.create(warehouse, hu, mastName);

        for (final TdoItem item : items) {
            result.getHuContents().add(mapToPostHuContent(item, hu, warehouse));
        }

        return result;
    }

    public PostHuContent mapToPostHuContent(final TdoItem item, final String hu, final String warehouse) {
        return PostHuContent.create(hu, warehouse, item.getSgtin(), item.getEan(), item.getItem());
    }

}
