package com.decathlon.log.rfid.pallet.ui.scan;

import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.google.common.collect.Lists;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ItemsTable extends JTable {

    private ItemTableModel model;

    public ItemsTable() {
        super();
        this.model = new ItemTableModel();
        this.setSize(800, 600);
        initialize();
    }

    private void initialize() {
        this.setModel(model);
        this.setIntercellSpacing(new Dimension(4, 4));
        this.setRowSelectionAllowed(false);
        this.setCellSelectionEnabled(false);

        this.setFillsViewportHeight(true);
        this.setBackground(Color.WHITE);

        this.getTableHeader().setReorderingAllowed(false);
        this.getTableHeader().setResizingAllowed(false);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setShowGrid(false);

        this.setDefaultRenderer(Object.class, new ItemTableCellRenderer());
        this.getTableHeader().setDefaultRenderer(new ItemsTableHeaderRender());

        this.setRowHeight(30);

        final java.util.List<TdoItem> data = Lists.newArrayList(
                createFakeTags(123457, "Model2", "Famille2", "Taille2", "Ean2", 1),
                createFakeTags(123458, "Model3", "Famille3", "Taille3", "Ean3", 59),
                createFakeTags(123459, "Model4", "Famille4", "Taille4", "Ean4", 60),
                createFakeTags(123460, "Model5", "Famille5", "Taille5", "Ean5", 65),
                createFakeTags(123461, "Model6", "Famille6", "Taille6", "Ean6", 79),
                createFakeTags(123462, "Model7", "Famille7", "Taille7", "Ean7", 80),
                createFakeTags(123463, "Model8", "Famille8", "Taille8", "Ean8", 81),
                createFakeTags(123464, "Model9", "Famille9", "Taille9", "Ean9", 89),
                createFakeTags(123465, "Model10", "Famille10", "Taille10", "Ean10", 90),
                createFakeTags(123466, "Model11", "Famille11", "Taille11", "Ean11", 92),
                createFakeTags(123467, "Model12", "Famille12", "Taille12", "Ean12", 95),
                createFakeTags(123468, "Model13", "Famille13", "Taille13", "Ean13", 96),
                createFakeTags(123469, "Model14", "Famille14", "Taille14", "Ean14", 98),
                createFakeTags(123470, "Model15", "Famille15", "Taille15", "Ean15", 99),
                createFakeTags(123471, "Model16", "Famille16", "Taille16", "Ean16", 100),
                createFakeTags(123472, "Model17", "Famille17", "Taille17", "Ean17", 110),
                createFakeTags(123456, "Model1", "Famille1", "Taille1", "Ean1", 0)
        );

//        setData(data);

        this.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.getColumnModel().getColumn(1).setPreferredWidth(160);
        this.getColumnModel().getColumn(2).setPreferredWidth(70);
        this.getColumnModel().getColumn(3).setPreferredWidth(130);
    }

    private TdoItem createFakeTags(final int itemCode, final String model,
                                   final String famille, final String taille,
                                   final String ean, final int count) {
        final TdoItem item = new TdoItem();
        item.setItem(String.valueOf(itemCode));
        item.setModel(model);
        item.setFamily(famille);
        item.setSize(taille);
        item.setEan(ean);
        item.setQtyTheo(100);
        item.setQtyRead(count);
        return item;
    }

    public void clear() {
        model.clear();
    }

    public void addData(final TdoItem tdoItem) {
        model.addReadItem(tdoItem);
    }

    public void setTheoreticalTags(final List<TdoItem> list) {
        model.setTheoreticalData(new ItemForTableDataMapper().map(list));
    }

    public int getExpectedTagCount() {
        return model.getExpectedItemTotalCount();
    }

    public int getReadTagCount() {
        return model.getExpectedReadTagCount();
    }

}