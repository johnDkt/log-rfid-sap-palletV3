package com.decathlon.log.rfid.pallet.ui.scan;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ItemTableCellRenderer extends DefaultTableCellRenderer {

    public static Color backGroundEven = new Color(201, 228, 255);
    public static Color backGroundOdd = new Color(185, 220, 255);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        Color bgColor = backGroundEven;
        if (row % 2 == 0) {
            bgColor = backGroundOdd;
        }

        if(column == 1 && row == 0) {
            final ItemForTableData item = ((ItemTableModel)table.getModel()).getExpectedAndDisplayedItems().get(row);
            ItemProgressPanel progressPanel = new ItemProgressPanel();
            progressPanel.setBackground(Color.LIGHT_GRAY);
            progressPanel.setOpaque(true);
            progressPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            progressPanel.setTotal(item.getQtyExpected());
            progressPanel.setQtyWithoutComputePercent(item.getQtyRead());
            return progressPanel;
        }

        if(column == 1 && row != 0) {
            final ItemForTableData item = ((ItemTableModel)table.getModel()).getExpectedAndDisplayedItems().get(row);
            ItemProgressPanel progressPanel = new ItemProgressPanel();
            progressPanel.setBackground(bgColor);
            progressPanel.setOpaque(true);
            progressPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            progressPanel.setTotal(item.getQtyExpected());
            progressPanel.setQtyAndComputePercent(item.getQtyRead());
            return progressPanel;
        }
        JLabel jLabel = new JLabel(String.valueOf(value));
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        jLabel.setFont(new Font("Dialog", Font.BOLD,  13));
        jLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK,1 ));
        jLabel.setOpaque(true);
        jLabel.setBackground(bgColor);
        return jLabel;
    }

}
