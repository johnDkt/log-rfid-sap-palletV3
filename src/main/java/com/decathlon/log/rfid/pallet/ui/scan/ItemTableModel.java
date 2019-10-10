package com.decathlon.log.rfid.pallet.ui.scan;

import com.decathlon.log.rfid.pallet.resources.ResourceManager;
import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import lombok.extern.log4j.Log4j;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class ItemTableModel extends AbstractTableModel {

    public static final String TEMPORARY_EAN = ResourceManager.getInstance().getString("ScanPanelLight.table.temporary");
    public static final String TEMPORARY_ITEM = ResourceManager.getInstance().getString("ScanPanelLight.table.multiples");
    public static final String OTHERS = ResourceManager.getInstance().getString("ScanPanelLight.table.others");

    private ExpectedAndDisplayedItems expectedAndDisplayedItems;
    private List<ItemReadForTableData> readItemsBeforeTheoreticalData;
    private List<String> readEpcAfterTheoreticalData;


    public ItemTableModel() {
        super();
        expectedAndDisplayedItems = new ExpectedAndDisplayedItems();
        readItemsBeforeTheoreticalData = new ArrayList<ItemReadForTableData>();
        readEpcAfterTheoreticalData = new ArrayList<String>();
    }

    @Override
    public int getRowCount() {
        return expectedAndDisplayedItems.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(final int columnIndex) {
        switch (columnIndex) {
            case 0:
                return ResourceManager.getInstance().getString("ScanPanel.table.item.column.header.item.label");
            case 1:
                return ResourceManager.getInstance().getString("ScanPanel.table.item.column.header.qty.label");
            case 2:
                return ResourceManager.getInstance().getString("ScanPanel.table.item.column.header.model.label");
            case 3:
                return ResourceManager.getInstance().getString("ScanPanel.table.item.column.header.ean.label");
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final ItemForTableData item = expectedAndDisplayedItems.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return item.getItemCode();
            case 1:
                return item.getQtyRead() + " / " + item.getQtyExpected();
            case 2:
                return item.getDescription();
            case 3:
                return item.getEans().get(0);
            default:
                return "";
        }
    }

    public void clear() {

        this.expectedAndDisplayedItems.clear();
        this.readItemsBeforeTheoreticalData.clear();
        this.readEpcAfterTheoreticalData.clear();

        fireTableDataChanged();
    }

    public void setTheoreticalData(final List<ItemForTableData> items) {
        expectedAndDisplayedItems.clear();
        expectedAndDisplayedItems.setTheoreticalData(items);

        refreshData();

        readItemsBeforeTheoreticalData.clear();

        fireTableDataChanged();
    }

    public void refreshData() {
        log.debug("refreshData after ws call");
        for (final ItemReadForTableData readBefore : readItemsBeforeTheoreticalData) {
            boolean isFound = expectedAndDisplayedItems.addReadItem(new TdoItem(readBefore.getItemCode(), readBefore.getEan()));
            log.debug("item read before wsCall = "+readBefore.toString());
            log.debug("is matching = "+isFound);
            if (isFound) {
                readEpcAfterTheoreticalData.add(readBefore.getEpc());
            }
        }

    }

    public void addReadItem(final TdoItem item) {
        log.trace("addReadItem : "+item.toString());
        log.trace("addReadItem : hasDataFromServer =" + expectedAndDisplayedItems.hasDataFromServer());

        if (item == null) {
            return;
        }
        if (expectedAndDisplayedItems.hasDataFromServer()) {
            log.trace("addReadItem : has data from server");
            log.trace("addReadItem : is sgtin " + item.getSgtin() + " match with expected : " + expectedAndDisplayedItems.addReadItem(item));
            if (expectedAndDisplayedItems.addReadItem(item)) {
                readEpcAfterTheoreticalData.add(item.getSgtin());
            }
        } else {
            log.trace("addReadItem : hasn't data from server , add in readItemsBeforeTheoreticalData");
            //expectedAndDisplayedItems.addReadItem(item); // sert à rien ?
            readItemsBeforeTheoreticalData.add(new ItemReadForTableData(item));
        }
        fireTableDataChanged();
    }

    public int getExpectedReadTagCount() {
        return readEpcAfterTheoreticalData.size();
    }

    public int getExpectedItemTotalCount() {
        int count = 0;

        for (final ItemForTableData itemForTableData : expectedAndDisplayedItems.getItems()) {
            count += itemForTableData.getQtyExpected();
        }

        return count;
    }

    public ExpectedAndDisplayedItems getExpectedAndDisplayedItems() {
        return this.expectedAndDisplayedItems;
    }

    public List<ItemReadForTableData> getReadItemsBeforeTheoreticalData() {
        return this.readItemsBeforeTheoreticalData;
    }

    public List<String> getReadEpcAfterTheoreticalData() {
        return this.readEpcAfterTheoreticalData;
    }

    public void setExpectedAndDisplayedItems(ExpectedAndDisplayedItems expectedAndDisplayedItems) {
        this.expectedAndDisplayedItems = expectedAndDisplayedItems;
    }
}
