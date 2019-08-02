package com.decathlon.log.rfid.pallet.scan.task;

import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import com.decathlon.log.rfid.pallet.service.ScannerService;
import org.apache.log4j.Logger;
import org.jdesktop.application.Task;

public class StopPanelCommandButtonsActionTask extends Task<Boolean, Void> {
    private static Logger LOGGER = Logger.getLogger(StopPanelCommandButtonsActionTask.class);

    private ScannerService scannerService;

    public StopPanelCommandButtonsActionTask() {
        super(RFIDPalletApp.getApplication());
        this.scannerService = ScannerService.getInstance();
    }

    /**
     * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
     */
    @Override
    protected Boolean doInBackground() throws Exception {
        return scannerService.getBoScanner().stop();
    }

    /**
     * @see org.jdesktop.application.Task#succeeded(java.lang.Object)
     */
    @Override
    protected void succeeded(Boolean result) {
        if (result) {
            LOGGER.debug("scanner_stopped_correctly");
        } else {
//			JOptionPane.showMessageDialog(RFIDPalletApp.getView().getFrame(),
//					ResourceManager.getInstance().getString("scanner.stop.error"),
//					ResourceManager.getInstance().getString("scanner.stop.error.title"), JOptionPane.ERROR_MESSAGE);
        }
    }

}
