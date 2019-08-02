package com.decathlon.log.rfid.pallet.ui.scan;

import javax.swing.*;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.plaf.metal.MetalScrollButton;
import java.awt.*;

public class ItemsScrollPane extends JScrollPane {

    private static final long serialVersionUID = 1L;

    private ItemsTable table;

    /**
     * This is the default constructor
     */
    public ItemsScrollPane() {
        super();
        initialize();
    }

    private void initialize() {
		this.setSize(500,500);
        this.setViewportView(getJTable());
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        this.setBackground(Color.WHITE);
        this.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.getVerticalScrollBar().setPreferredSize(new Dimension(40, 40));
        this.getVerticalScrollBar().setUI(new PersoScrollBarUi());
    }

    private Component getJTable() {
        if (table == null) {
            table = new ItemsTable();
        }
        return table;
    }

    public ItemsTable getItemsTable() {
        return this.table;
    }

    /**
     * Boutons haut et bas aggrandis
     *
     * @author z01scarp
     */
    private class PersoScrollBarUi extends MetalScrollBarUI {
        protected JButton createDecreaseButton(int orientation) {
            return new MetalScrollButton(orientation, 40, true) {
                public Dimension getPreferredSize() {
                    return new Dimension(40, 40);
                }
            };
        }

        protected JButton createIncreaseButton(int orientation) {
            return new MetalScrollButton(orientation, 40, true) {
                public Dimension getPreferredSize() {
                    return new Dimension(40, 40);
                }
            };
        }

    }

}
