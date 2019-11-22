package com.decathlon.log.rfid.pallet.scan.task;

import com.decathlon.connectJavaIntegrator.mqtt.RFIDConnectJavaMqttInstance;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.CommandManager;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.commonCommandsObject.deviceStatus.DeviceStatus;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.sendToConnectJava.ConnectCommandToSend;
import com.decathlon.connectJavaIntegrator.utils.Utils;
import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import org.apache.log4j.Logger;
import org.jdesktop.application.Task;


public class StartPanelCommandButtonsActionTask extends Task<Object, Void> {
    private static Logger LOGGER = Logger.getLogger(StartPanelCommandButtonsActionTask.class);

    private RFIDConnectJavaMqttInstance RFIDConnectJavaInstance;

    /**
     * Constructor.
     *
     */
    public StartPanelCommandButtonsActionTask() {
        super(RFIDPalletApp.getApplication());
        this.RFIDConnectJavaInstance = RFIDPalletApp.RFIDConnectJavaInstance;
    }

    /**
     * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
     */
    @Override
    protected Object doInBackground() throws Exception {

        if(Utils.isNotNull(RFIDConnectJavaInstance)){
            LOGGER.debug("START NEW READ");
            if(DeviceStatus.getIsConnected()){
                RFIDConnectJavaInstance.sendCommand(ConnectCommandToSend.createCommand(CommandManager.COMMAND_ACTION.START_CONTINUOUS_READ));
            }else{
                LOGGER.warn("impossible to start because device is not connected");
            }
        }else{
            LOGGER.warn("RFIDConnectInstance was null");
        }
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
