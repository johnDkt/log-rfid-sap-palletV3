package com.decathlon.log.rfid.pallet.ui.indicator;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * @author z21bcoll
 */
public class MiniBusyIndicator extends JComponent implements ActionListener, Runnable {

    /**
     * .
     */
    private static final long serialVersionUID = 1L;
    static Logger LOGGER = Logger.getLogger(MiniBusyIndicator.class);
    private final int nBars = 8;
    private final float barWidth = 3;
    private final float outerRadius = 12;
    private final float innerRadius = 4;
    private final int trailLength = 4;
    private final float barGray = 200f; // shade of gray, 0-255
    Thread thread;
    Point point;
    private int frame = -1; // animation frame index
    private Timer timer = null;

    /**
     *
     *
     */
    public MiniBusyIndicator() {
        timer = new Timer(65, this); // 65ms = animation rate
    }

    public void actionPerformed(ActionEvent ignored) {
        frame += 1;
        repaint();
    }

    /**
     * @param p
     */
    public void startAnim(Point p) {
//		timer = new Timer(65, this); // 65ms = animation rate
//		point = p;
//		thread = new Thread(this);
//		thread.start();
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
//		setVisible(true);
//		requestFocusInWindow();
//		
//		timer.start();
    }

    /**
     *
     *
     */
    public void stopAnim() {
//		setVisible(false);
//		timer.stop();
    }

    /**
     *
     *
     */
    public void reStart() {
//		stopAnim();
//		
//		timer = new Timer(125, this); // 65ms = animation rate
//		startAnim(point);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // first horizontal bar (on the right of the center)
        RoundRectangle2D bar = new RoundRectangle2D.Float(
                innerRadius, -barWidth / 2, outerRadius, barWidth, barWidth, barWidth);
        // x,         y,          width,       height,   arc width,arc height

        // angle in radians
        double angle = Math.PI * 2.0 / nBars; // between bars


        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.red);

        g2d.translate(point.getX() - 100, point.getY() + 10); // center the original point
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // rendering hints

        // for each bar
        for (int i = 0; i < nBars; i++) {
            // compute bar i's color based on the frame index
//			Color barColor = new Color((int) barGray, (int) barGray, (int) barGray);
            Color barColor = new Color((int) barGray, 0, 0);
            if (frame != -1) {
                for (int t = 0; t < trailLength; t++) {
                    if (i == ((frame - t + nBars) % nBars)) {
                        float tlf = trailLength;
                        float pct = 1.0f - ((tlf - t) / tlf);
                        int gray = (int) ((barGray - (pct * barGray)) + 0.5f);
                        barColor = new Color(gray, 200, 100); // red head and green trail
//						barColor = new Color(gray, gray, gray);
                    }
                }
            }
            // draw the bar
            g2d.setColor(barColor);
            g2d.fill(bar);
            g2d.rotate(angle);
        }
    }
}
