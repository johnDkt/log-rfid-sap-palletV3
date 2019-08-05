package com.decathlon.log.rfid.pallet.scan.task;

import com.decathlon.connectJavaIntegrator.factory.RFIDConnectConnectorFactoryList;
import com.decathlon.connectJavaIntegrator.tcp.RFIDConnectConnector;
import com.decathlon.connectJavaIntegrator.tcp.handleCommands.CommandManager;
import com.decathlon.connectJavaIntegrator.tcp.handleCommands.ConnectCommandToSend;
import com.decathlon.connectJavaIntegrator.utils.Utils;
import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import org.apache.log4j.Logger;
import org.jdesktop.application.Task;

import java.io.IOException;

public class StopPanelCommandButtonsActionTask extends Task<Object, Void> {
    private static Logger LOGGER = Logger.getLogger(StopPanelCommandButtonsActionTask.class);

    private RFIDConnectConnector RFIDConnectInstance;

    public StopPanelCommandButtonsActionTask() {
        super(RFIDPalletApp.getApplication());
        try {
            this.RFIDConnectInstance = RFIDConnectConnectorFactoryList.getInstance();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
     */
    @Override
    protected Object doInBackground() throws Exception {

        if(Utils.isNotNull(RFIDConnectInstance)){
            LOGGER.debug("START NEW READ");
            RFIDConnectInstance.sendCommand(ConnectCommandToSend.createCommand(CommandManager.COMMAND_ACTION.START_CONTINUOUS_READ));
        }else{
            LOGGER.warn("connectInstance was null");
        }
        return null;
    }

    /**
     * @see org.jdesktop.application.Task#succeeded(java.lang.Object)
     */
    @Override
    protected void succeeded(Object result) {
       /*
       if (result) {
            LOGGER.debug("scanner_stopped_correctly");
        } else {
			JOptionPane.showMessageDialog(RFIDPalletApp.getView(),
                    ResourceManager.getInstance().getString("scanner.stop.error"),
                    ResourceManager.getInstance().getString("scanner.stop.error.title"), JOptionPane.ERROR_MESSAGE);
        }
        */
    }

}
