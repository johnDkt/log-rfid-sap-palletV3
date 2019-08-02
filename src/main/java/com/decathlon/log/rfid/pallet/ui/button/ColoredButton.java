package com.decathlon.log.rfid.pallet.ui.button;

import javax.swing.*;
import java.awt.*;

/**
 * @author z01scarp
 */
public class ColoredButton extends JButton {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Color currentColor;
    private Color color;

    public ColoredButton(Color color) {
        super();
        this.color = color;
        this.currentColor = this.color;
        this.setFont(new Font("Dialog", Font.BOLD, 12));
    }

    public ColoredButton() {
        super();
    }


    public void paint(Graphics g) {

        super.paint(g);
        Color base = this.currentColor;
        int alpha = 70;

        if (base == null) {
            base = Color.WHITE;
            alpha = 0;
        }
        g.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * Enables (or disables) the button.
     *
     * @param b true to enable the button, otherwise false
     */
    public void setEnabled(boolean b) {

        if (b) {
            this.currentColor = this.color;
        } else {
            this.currentColor = Color.GRAY;
        }

        super.setEnabled(b);
    }
}
