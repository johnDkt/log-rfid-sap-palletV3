package com.decathlon.log.rfid.pallet.ui.scan;


import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;


public class ItemsTableHeaderRender implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel jLabel = new JLabel(String.valueOf(value));
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        jLabel.setFont(new Font("Dialog", Font.BOLD,  13));
        jLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK,1 ));
        jLabel.setOpaque(true);
        jLabel.setBackground(Color.LIGHT_GRAY);
        return jLabel;
    }

}
