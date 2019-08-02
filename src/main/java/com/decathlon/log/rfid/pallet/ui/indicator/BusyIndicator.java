package com.decathlon.log.rfid.pallet.ui.indicator;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Hans Muller.
 */
/* This component is intended to be used as a GlassPane.  It's
 * start method makes this component visible, consumes mouse
 * and keyboard input, and displays a spinning activity indicator 
 * animation.  The stop method makes the component not visible.
 * The code for rendering the animation was lifted from 
 * org.jdesktop.swingx.painter.BusyPainter.  I've made some
 * simplifications to keep the example small.
 */
public class BusyIndicator extends JComponent implements ActionListener {
    /**
     * .
     */
    private static final long serialVersionUID = 1L;
    private final int nBars = 8;
    private final float barWidth = 6;
    private final float outerRadius = 28;
    private final float innerRadius = 12;
    private final int trailLength = 4;
    private final float barGray = 200f; // shade of gray, 0-255
    private final Timer timer = new Timer(65, this); // 65ms = animation rate
    private int frame = -1; // animation frame index

    /**
     *
     *
     */
    public BusyIndicator() {
        setBackground(Color.WHITE);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MouseInputListener blockMouseEvents = new MouseInputAdapter() {
        };
        KeyListener blockKeyEvents = new KeyAdapter() {
        };
        addMouseMotionListener(blockMouseEvents);
        addMouseListener(blockMouseEvents);
        addKeyListener(blockKeyEvents);
    }

    public void actionPerformed(ActionEvent ignored) {
        frame += 1;
        repaint();
    }

    void start() {
        setVisible(true);
        requestFocusInWindow();
        timer.start();
    }

    void stop() {
        setVisible(false);
        timer.stop();
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
        g2d.translate(getWidth() / 2, getHeight() / 2); // center the original point
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // rendering hints
                RenderingHints.VALUE_ANTIALIAS_ON);


        // == Added for white bg
        // gets the current clipping area
        Rectangle clip = g.getClipBounds();

        // sets a 65% translucent composite
        AlphaComposite alpha = AlphaComposite.SrcOver.derive(0.65f);
        Composite composite = g2d.getComposite();
        g2d.setComposite(alpha);

        // fills the background
        g2d.setColor(getBackground());
        g2d.fillRect(clip.x, clip.y, clip.width, clip.height);


        // for each bar
        for (int i = 0; i < nBars; i++) {
            // compute bar i's color based on the frame index
            Color barColor = new Color((int) barGray, (int) barGray, (int) barGray);
            if (frame != -1) {
                for (int t = 0; t < trailLength; t++) {
                    if (i == ((frame - t + nBars) % nBars)) {
                        float tlf = trailLength;
                        float pct = 1.0f - ((tlf - t) / tlf);
                        int gray = (int) ((barGray - (pct * barGray)) + 0.5f);
                        barColor = new Color(gray, gray, gray);
                    }
                }
            }
            // draw the bar
            g2d.setColor(barColor);
            g2d.fill(bar);
            g2d.rotate(angle);
        }

        g2d.setComposite(composite);
    }
}
