package com.decathlon.log.rfid.pallet.scan.task;

import org.apache.log4j.Logger;

import javax.swing.*;

public class PlayTask implements Runnable {
    private static Logger LOGGER = Logger.getLogger(PlayTask.class);

    private boolean start = false;
    private boolean visible = false;
    private JLabel label;

    public PlayTask(final JLabel label) {
        this.label = label;
    }

    public void stop() {
        this.start = false;
        this.visible = false;
        this.label.setVisible(false);
    }

    public void start() {
        Thread t = new Thread(this, "TagsHandler-Thread");
        start = true;
        t.start();
    }

    @Override
    public void run() {

        while (start) {
            if (visible) {
                visible = false;
                label.setVisible(false);
            } else {
                visible = true;
                label.setVisible(true);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                LOGGER.error("Error while sleeping in tags handler thread", e);
            }
        }
    }

}