package com.decathlon.log.rfid.pallet.manager;


import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class DoItemManager {

    public List<TdoItem> gatherDoItemByEan(final List<TdoItem> doItemToFilter) {
        final List<TdoItem> result = new ArrayList<TdoItem>();
        if (doItemToFilter.isEmpty()) {
            return result;
        }

        if (containsOnlyEmptyEan(doItemToFilter)) {
            return result;
        }

        for (final TdoItem doItem : doItemToFilter) {
            if (!containsEan(result, doItem.getEan())) {
                result.add(getDoItemByEanFromList(doItemToFilter, doItem.getEan()));
            }
        }

        return result;
    }

    private boolean containsOnlyEmptyEan(final List<TdoItem> doItems) {
        final List<TdoItem> filter = Lists.newArrayList(Collections2.filter(doItems, new Predicate<TdoItem>() {
            @Override
            public boolean apply(final TdoItem tdoItem) {
                return tdoItem.getEan().isEmpty();
            }
        }));

        return filter.size() == doItems.size();
    }

    private boolean containsEan(final List<TdoItem> doItems, final String ean) {
        return !doItems.isEmpty() && !Collections2.filter(doItems, new Predicate<TdoItem>() {
            @Override
            public boolean apply(TdoItem tdoItem) {
                return ean.equals(tdoItem.getEan());
            }
        }).isEmpty();
    }

    private TdoItem getDoItemByEanFromList(final List<TdoItem> tdoItems, final String ean) {
        final List<TdoItem> filter = Lists.newArrayList(Collections2.filter(tdoItems, new Predicate<TdoItem>() {
            @Override
            public boolean apply(TdoItem tdoItem) {
                return ean.equals(tdoItem.getEan());
            }
        }));

        final List<Integer> quantities = Lists.transform(filter, new Function<TdoItem, Integer>() {
            @Override
            public Integer apply(final TdoItem tdoItem) {
                return tdoItem.getQtyTheo();
            }
        });

        return createDoItemFromAnotherOneWithAFinalQty(filter.get(0), getFinalQuantity(quantities));
    }

    private int getFinalQuantity(final List<Integer> quantities) {
        int result = 0;
        for (Integer quantity : quantities) {
            result += quantity;
        }
        return result;
    }

    private TdoItem createDoItemFromAnotherOneWithAFinalQty(final TdoItem referred, final int qty) {
        final TdoItem result = new TdoItem();
        result.setEan(referred.getEan());
        result.setItem(referred.getItem());
        result.setRawDescription(referred.getRawDescription());
        result.setEncodedEan(referred.getEncodedEan());
        result.setQtyTheo(qty);
        return result;
    }

}
