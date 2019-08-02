package com.decathlon.log.rfid.pallet.ui.glassPane;

import javax.swing.*;
import java.awt.*;

public class DarkGlassPane extends JPanel {

    public static final Color GLASS_PANE_BG_COLOR = new Color(0, 0, 0, 100);

    public DarkGlassPane() {
        super();
        initialize();
    }

    public void initialize() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        g.setColor(GLASS_PANE_BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}
