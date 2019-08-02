package com.decathlon.log.rfid.pallet.ui.scan;

import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class ItemTableModelTest {

    @Test
    public void clear_WithoutData_theTableStillEmpty() throws Exception {
        final ItemTableModel model = new ItemTableModel();

        model.clear();

        assertTrue(model.getExpectedAndDisplayedItems().isEmpty());
        assertTrue(model.getReadItemsBeforeTheoreticalData().isEmpty());
    }

    @Test
    public void clear_WithData_clearTheTable() throws Exception {
        final ItemTableModel model = createGenericItemTableModel();

        model.clear();

        assertTrue(model.getExpectedAndDisplayedItems().isEmpty());
        assertTrue(model.getReadItemsBeforeTheoreticalData().isEmpty());
    }

    @Test
    public void addReadItem_WithANullItem_NothingHappened() throws Exception {
        final ItemTableModel model = createGenericItemTableModel();

        model.addReadItem(null);

        assertTrue(model.getReadItemsBeforeTheoreticalData().isEmpty());
    }

    @Test
    public void addReadItem_WithAnEmptyItem_NothingHappened() throws Exception {
        final ItemTableModel model = createGenericItemTableModel();

        final TdoItem tdoItem = new TdoItem();
        model.addReadItem(tdoItem);

        assertTrue(model.getReadItemsBeforeTheoreticalData().isEmpty());
    }

    @Test
    public void addReadItem_WithAnExpectedItem_IncrementsTheQuantity() throws Exception {
        final ItemTableModel model = createGenericItemTableModel();

        model.addReadItem(createTdoItem("30396062C3B512C0001A8101", "0000000000001", "0000001"));
        model.addReadItem(createTdoItem("30396062C3B512C0001A8102", "0000000000001", "0000001"));

        assertEquals(0, model.getExpectedAndDisplayedItems().getUselessItem().getQtyRead());
        assertItemQtyIsEqualsTo(2, model.getExpectedAndDisplayedItems().getItems(), "0000000000001");
    }

    @Test
    public void addReadItem_WithANotExpectedItem_IncrementsUselessItem() throws Exception {
        final ItemTableModel model = createGenericItemTableModel();

        model.addReadItem(createTdoItem("30396062C3B512C0001A8101", "1000000000000", "1000000"));
        model.addReadItem(createTdoItem("30396062C3B512C0001A8102", "2000000000000", "2000000"));

        assertEquals(2, model.getExpectedAndDisplayedItems().getUselessItem().getQtyRead());
        assertItemQtyIsEqualsTo(0, model.getExpectedAndDisplayedItems().getItems());
    }

    @Test
    public void addReadItem_WithoutTheoreticalItem_IncrementUselessItemAndKeepTheReadItem() throws Exception {
        final ItemTableModel model = new ItemTableModel();

        model.addReadItem(createTdoItem("30396062C3B512C0001A8101", "0000000000001", "0000001"));

        assertEquals(1, model.getExpectedAndDisplayedItems().size());
        assertEquals(1, model.getExpectedAndDisplayedItems().getUselessItem().getQtyRead());
        assertEquals(0, model.getExpectedAndDisplayedItems().getItems().size());
        assertEquals(1, model.getReadItemsBeforeTheoreticalData().size());

        model.addReadItem(createTdoItem("30396062C3B512C0001A8102", "0000000000002", "0000002"));

        assertEquals(1, model.getExpectedAndDisplayedItems().size());
        assertEquals(2, model.getExpectedAndDisplayedItems().getUselessItem().getQtyRead());
        assertEquals(0, model.getExpectedAndDisplayedItems().getItems().size());
        assertEquals(2, model.getReadItemsBeforeTheoreticalData().size());

    }

    @Test
    public void setTheoreticalData_WhenThereIsNotReadItems_ChangeDisplayingItems() throws Exception {
        final ItemTableModel model = createGenericItemTableModel();

        assertEquals(3, model.getExpectedAndDisplayedItems().getItems().size());
        assertTrue(model.getReadItemsBeforeTheoreticalData().isEmpty());
        assertTrue(model.getReadEpcAfterTheoreticalData().isEmpty());
    }

    @Test
    public void setTheoreticalData_WhenThereIsReadItems_UpdateDisplaying() throws Exception {
        final ItemTableModel model = new ItemTableModel();

        model.addReadItem(createTdoItem("30396062C3B512C0001A8101", "0000000000001", "0000001"));
        model.addReadItem(createTdoItem("30396062C3B512C0001A8102", "0000000000001", "0000001"));
        model.addReadItem(createTdoItem("30396062C3B512C0001A8103", "0000000000002", "0000001"));
        model.addReadItem(createTdoItem("30396062C3B512C0001A8104", "1000000000000", "1000000"));

        assertEquals(4, model.getExpectedAndDisplayedItems().getUselessItem().getQtyRead());
        assertEquals(4, model.getReadItemsBeforeTheoreticalData().size());
        assertTrue(model.getReadEpcAfterTheoreticalData().isEmpty());

        model.setTheoreticalData(Lists.newArrayList(
                new ItemForTableData("0000001", Lists.newArrayList("0000000000001", "0000000000002"), 5, "desc"),
                new ItemForTableData("000002", Lists.newArrayList("0000000000003"), 3, "desc"),
                new ItemForTableData("0000003", Lists.newArrayList("0000000000004", "0000000000005"), 1, "desc")
        ));

        //Nb items
        assertEquals(1, model.getExpectedAndDisplayedItems().getUselessItem().getQtyRead());
        assertEquals(0, model.getReadItemsBeforeTheoreticalData().size());
        assertEquals(3, model.getReadEpcAfterTheoreticalData().size());
        assertEquals(4, model.getExpectedAndDisplayedItems().size());

        //Quantity by item
        assertItemQtyIsEqualsTo(3, model.getExpectedAndDisplayedItems().getItems(), "0000000000001");
        assertItemQtyIsEqualsTo(0, model.getExpectedAndDisplayedItems().getItems(), "0000000000003");
        assertItemQtyIsEqualsTo(0, model.getExpectedAndDisplayedItems().getItems(), "0000000000004");

        //Epc keep
        assertTrue(model.getReadEpcAfterTheoreticalData().contains("30396062C3B512C0001A8101"));
        assertTrue(model.getReadEpcAfterTheoreticalData().contains("30396062C3B512C0001A8102"));
        assertTrue(model.getReadEpcAfterTheoreticalData().contains("30396062C3B512C0001A8103"));

        //Epc doesn't keep
        assertFalse(model.getReadEpcAfterTheoreticalData().contains("30396062C3B512C0001A8104"));
    }

    @Test
    public void setTheoreticalData_WithEan21AndEan200_UpdateDisplaying() throws Exception{
        final ItemTableModel model = new ItemTableModel();
        model.addReadItem(createTdoItem("30396062C3B512C0001A8101","2001212345676","1234567"));
        model.addReadItem(createTdoItem("30396062C3B512C0001A8102","2102212345676","1234567"));
        model.addReadItem(createTdoItem("30396062C3B512C0001A8103","2001212345676", "1234567"));

        model.setTheoreticalData(Lists.newArrayList(
                new ItemForTableData("0221234", Lists.newArrayList("1234567898767", "1923182392812"), 3, "blabla"),
                new ItemForTableData("000001", Lists.newArrayList("1234567898767", "1923182392812"), 3, "blabla"),
                new ItemForTableData("121234", Lists.newArrayList("1234567898767", "1923182392812"), 3, "blabla")
        ));

        //only for eanHead 200
        assertTrue(model.getReadEpcAfterTheoreticalData().contains("30396062C3B512C0001A8101"));
        assertTrue(model.getReadEpcAfterTheoreticalData().contains("30396062C3B512C0001A8103"));

        //only for eanHead 21
        assertTrue(model.getReadEpcAfterTheoreticalData().contains("30396062C3B512C0001A8102"));

        assertItemQtyIsEqualsTo(2, model.getExpectedAndDisplayedItems().getItems(),"121234");
        assertItemQtyIsEqualsTo(1, model.getExpectedAndDisplayedItems().getItems(),"0221234");
        assertItemQtyIsEqualsTo(0, model.getExpectedAndDisplayedItems().getItems(),"000001");

    }

    @Test
    public void getExpectedItemTotalCount_WithoutTheoreticalItems_IsEqualsToZero() throws Exception {
        final ItemTableModel model = new ItemTableModel();

        assertEquals(0, model.getExpectedItemTotalCount());
    }

    @Test
    public void getExpectedItemTotalCount_WithTheoreticalItems_IsEqualsToZero() throws Exception {
        final ItemTableModel model = createGenericItemTableModel();

        assertEquals(9, model.getExpectedItemTotalCount());
    }


    @Test
    public void getExpectedReadTagCount_WithoutGoodItems_IsEqualsToZero() throws Exception {
        final ItemTableModel model = new ItemTableModel();

        assertEquals(0, model.getExpectedReadTagCount());
    }

    @Test
    public void getExpectedReadTagCount_WithGoodItems_IsEqualsToZero() throws Exception {
        final ItemTableModel model = createGenericItemTableModel();

        model.addReadItem(createTdoItem("30396062C3B512C0001A8101", "0000000000001", "0000001"));
        model.addReadItem(createTdoItem("30396062C3B512C0001A8102", "0000000000001", "0000001"));
        model.addReadItem(createTdoItem("30396062C3B512C0001A8103", "0000000000002", "0000001"));
        model.addReadItem(createTdoItem("30396062C3B512C0001A8104", "1000000000000", "1000000"));

        assertEquals(3, model.getExpectedReadTagCount());
    }

    private TdoItem createTdoItem(final String epc, final String ean, final String itemCode) {
        final TdoItem tdoItem = new TdoItem();
        tdoItem.setEan(ean);
        tdoItem.setQtyRead(1);
        tdoItem.setFamily("family");
        tdoItem.setModel("model");
        tdoItem.setSize("size");
        tdoItem.setSgtin(epc);
        tdoItem.setItem(itemCode);
        return tdoItem;
    }

    private ItemTableModel createGenericItemTableModel() {
        final ItemTableModel model = new ItemTableModel();
        model.setTheoreticalData(Lists.newArrayList(
                new ItemForTableData("0000001", Lists.newArrayList("0000000000001", "0000000000002"), 5, "desc"),
                new ItemForTableData("000002", Lists.newArrayList("0000000000003"), 3, "desc"),
                new ItemForTableData("0000003", Lists.newArrayList("0000000000004", "0000000000005"), 1, "desc")
        ));
        return model;
    }

    private void assertItemQtyIsEqualsTo(final int expectedQuantity, final List<ItemForTableData> items, final String oneOfEanOrItemCode) {
        final Collection<ItemForTableData> filter = Collections2.filter(items, new Predicate<ItemForTableData>() {
            @Override
            public boolean apply(ItemForTableData itemForTableData) {
                return itemForTableData.hasEan(oneOfEanOrItemCode) || oneOfEanOrItemCode.equals(itemForTableData.getItemCode());
            }
        });

        if (filter.isEmpty()) {
            fail("Not item found with the ean/item : " + oneOfEanOrItemCode);
        }

        final ItemForTableData next = filter.iterator().next();
        if (expectedQuantity != next.getQtyRead()) {
            fail("The quantity read [ " + next.getQtyRead() + " ] is not equals to the quantity given [ " + expectedQuantity + " ]");
        }

    }

    private void assertItemQtyIsEqualsTo(final int expectedQty, final List<ItemForTableData> items) {
        final Collection<ItemForTableData> filter = Collections2.filter(items, new Predicate<ItemForTableData>() {
            @Override
            public boolean apply(ItemForTableData itemForTableData) {
                return itemForTableData.getQtyRead() != expectedQty;
            }
        });

        if (!filter.isEmpty()) {
            fail("There are some items with quantity not equals to [ " + expectedQty + " ]");
        }

    }

}