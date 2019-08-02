package com.decathlon.log.rfid.pallet.ui.indicator;

import org.jdesktop.application.Task;
import org.jdesktop.application.Task.InputBlocker;

/**
 * @author Hans Muller. +z21bcoll
 */
public class BusyIndicatorInputBlocker extends InputBlocker {
    private BusyIndicator busyIndicator = null;

    /**
     * @param task
     * @param busyIndicator
     */
    public BusyIndicatorInputBlocker(Task task, BusyIndicator busyIndicator) {
        super(task, Task.BlockingScope.WINDOW, busyIndicator);
        this.busyIndicator = busyIndicator;
    }

    @Override
    protected void block() {
        busyIndicator.start();
    }

    @Override
    protected void unblock() {
        busyIndicator.stop();
    }
}