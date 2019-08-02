package com.decathlon.log.rfid.pallet.ui.scan;

import javax.swing.*;
import java.awt.*;

public class ItemProgressPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    /*0%;<4%;4 \u00E0 10%;10 \u00E0 20%;20 \u00E0 40%;>40
    graph.10.indicator=58;59;61;62;63;64
    graph.10.color=;;;;;*/
    private static final Color COLOR_1 = new Color(255, 0, 0);  //  @jve:decl-index=0:
    private static final Color COLOR_2 = new Color(255, 153, 0);  //  @jve:decl-index=0:
    private static final Color COLOR_3 = new Color(255, 255, 153);  //  @jve:decl-index=0:
    private static final Color COLOR_4 = new Color(0, 255, 0);  //  @jve:decl-index=0:
    private static final Color COLOR_5 = new Color(0, 128, 0);  //  @jve:decl-index=0:
    private static final Color COLOR_6 = new Color(0, 90, 0);  //  @jve:decl-index=0:
    private static final Color COLOR_7 = Color.BLUE;  //  @jve:decl-index=0:
    private static final Color COLOR_FG_1 = Color.BLACK;  //  @jve:decl-index=0:
    private static final Color COLOR_FG_2 = Color.BLACK;  //  @jve:decl-index=0:
    private static final Color COLOR_FG_3 = Color.BLACK;
    private static final Color COLOR_FG_4 = Color.BLACK;  //  @jve:decl-index=0:
    private static final Color COLOR_FG_5 = Color.WHITE;  //  @jve:decl-index=0:
    private static final Color COLOR_FG_6 = Color.WHITE;  //  @jve:decl-index=0:
    private static final Color COLOR_FG_7 = Color.WHITE;  //  @jve:decl-index=0:
    private Color bgColor = Color.BLUE;  //  @jve:decl-index=0:
    private Color fgColor = Color.BLACK;  //  @jve:decl-index=0:
    private JLabel totalLabel = null;
    private JLabel dynamicLabel = null;

    private int margin = 0;
    private int total;
    private int qty;
    private double percent = -1;

    /**
     * This is the default constructor
     */
    public ItemProgressPanel() {
        super();
        initialize();
    }

    /**
     * This is the default constructor
     */
    public ItemProgressPanel(Color bgColor, int total, int qty) {
        super();
        this.bgColor = bgColor;
        initialize();
    }

    public void setTotal(int total) {
        this.total = total;
        this.totalLabel.setText("");
    }

    public void computePercent() {
        double oldPercent = this.percent;
        if (this.total == 0) {
            this.percent = 100;
        } else {
            this.percent = (double) this.qty / (double) this.total * 100;
        }

        if (oldPercent != this.percent) {

            if (percent < 60) {
                bgColor = COLOR_1;
                fgColor = COLOR_FG_1;
            } else if (percent < 80) {
                bgColor = COLOR_2;
                fgColor = COLOR_FG_2;
            } else if (percent < 90) {
                bgColor = COLOR_3;
                fgColor = COLOR_FG_3;
            } else if (percent < 96) {
                bgColor = COLOR_4;
                fgColor = COLOR_FG_4;
            } else if (percent < 100) {
                bgColor = COLOR_5;
                fgColor = COLOR_FG_5;
            } else if (percent == 100) {
                bgColor = COLOR_6;
                fgColor = COLOR_FG_6;
            } else {
                bgColor = COLOR_7;
                fgColor = COLOR_FG_7;
            }

            this.totalLabel.setForeground(this.fgColor);
            this.dynamicLabel.setForeground(this.fgColor);

            repaint();
        }

    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {

        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints2.insets = new Insets(0, 2, 0, 0);
        gridBagConstraints2.gridy = 1;

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.weighty = 1.0D;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1D;
        gridBagConstraints.insets = new Insets(0, 0, 0, 2);
        gridBagConstraints.gridy = 1;

        dynamicLabel = new JLabel();
        dynamicLabel.setText("0");

        totalLabel = new JLabel();
        totalLabel.setText("300");

        this.setSize(636, 94);
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.LIGHT_GRAY);

        this.add(totalLabel, gridBagConstraints);
        this.add(dynamicLabel, gridBagConstraints2);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.getBackground();
        g.setColor(this.bgColor);
        int w = percent > 100 ? this.getWidth() : (int) Math.round((this.getWidth() * percent) / 100);
        g.fillRect(0, this.margin, w, this.getHeight() - this.margin);


    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public int getQty() {
        return this.qty;
    }

    public void setQtyAndComputePercent(int qty) {
        this.qty = qty;
        this.dynamicLabel.setText(qty + " / " + this.total);
        computePercent();
    }

    public void setQtyWithoutComputePercent(int qty) {
        this.qty = qty;
        this.dynamicLabel.setText(qty + " / " + this.total);
        this.dynamicLabel.setForeground(Color.white);
    }


    public void reset() {
        this.setTotal(0);
        this.setQtyAndComputePercent(0);
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
