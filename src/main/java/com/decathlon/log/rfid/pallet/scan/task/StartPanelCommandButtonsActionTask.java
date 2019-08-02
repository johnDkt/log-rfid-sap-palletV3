package com.decathlon.log.rfid.pallet.scan.task;

import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import com.decathlon.log.rfid.pallet.service.ScannerService;
import org.apache.log4j.Logger;
import org.jdesktop.application.Task;


public class StartPanelCommandButtonsActionTask extends Task<Object, Void> {
    private static Logger LOGGER = Logger.getLogger(StartPanelCommandButtonsActionTask.class);

    private int timeout;

    private ScannerService scannerService;

    /**
     * Constructor.
     *
     * @param timeout
     */
    public StartPanelCommandButtonsActionTask(int timeout) {
        super(RFIDPalletApp.getApplication());
        this.timeout = timeout;
        this.scannerService = ScannerService.getInstance();
    }

    /**
     * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
     */
    @Override
    protected Object doInBackground() throws Exception {
        LOGGER.debug("START NEW READ");
        LOGGER.debug("READ timeout = " + timeout);
        scannerService.getBoScanner().startNewRead(timeout, true);
        LOGGER.debug("END NEW READ");
        return null;
    }

    /**
     * @see org.jdesktop.application.Task#succeeded(java.lang.Object)
     */
    @Override
    protected void succeeded(Object result) {
        super.succeeded(result);
    }

}
