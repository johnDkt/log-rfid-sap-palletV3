package com.decathlon.log.rfid.pallet.autovalidation.task;

import com.decathlon.log.rfid.pallet.autovalidation.AutoValidationPropertiesHolder;
import lombok.extern.log4j.Log4j;

import java.util.TimerTask;

@Log4j
public class AutoValidationCountdownTimerTask extends TimerTask {

    private final long autoValidationTimeout;
    private final long autoValidationCountDown;

    private int numberOfCalls;

    public AutoValidationCountdownTimerTask(final long timeToLog) {
        this.numberOfCalls = 0;
        this.autoValidationCountDown = timeToLog;
        this.autoValidationTimeout = AutoValidationPropertiesHolder.getInstance().getTimeout();
    }

    @Override
    public void run() {
        long timeLeft = autoValidationTimeout - (numberOfCalls * autoValidationCountDown);
        ++numberOfCalls;
        log.debug("Auto validation in " + (timeLeft / 1000) + " seconds");
    }

}
