package com.decathlon.log.rfid.pallet.ui.panel;

import com.decathlon.connectJavaIntegrator.factory.CJI;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.CommandManager;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.commonCommandsObject.deviceStatus.DeviceStatus;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.sendToConnectJava.ConnectCommandToSend;
import com.decathlon.connectJavaIntegrator.utils.Utils;
import com.decathlon.log.rfid.pallet.constants.ColorConstants;
import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import com.decathlon.log.rfid.pallet.resources.ResourceManager;
import com.decathlon.log.rfid.pallet.ui.button.ColoredButton;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;

public class ShutdownJDialog extends JDialog {

    private static final Color SHUTDOWN_BG_COLOR = Color.WHITE;

    private ActionMap actionMap;

    private JFrame owner;

    public ShutdownJDialog(final JFrame owner) {
        super(owner);
        this.owner = owner;
        initialize();
    }

    private void initialize() {
        if (actionMap == null) {
            actionMap = RFIDPalletApp.getApplication().getContext().getActionMap(ShutdownJDialog.class, this);
        }

        setResizable(false);
        setUndecorated(true);
        setModal(true);
        setModalityType(JDialog.ModalityType.APPLICATION_MODAL);

        setContentPane(getMainPanel());
    }

    private JPanel getMainPanel() {
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new MigLayout("ins 10, fill", "[50%][50%]", "[][30%!]"));
        mainPanel.add(getMessage(), "growy, span, center, w 60%");
        mainPanel.add(getNoButton(), "newline, growy, center, w 40%");
        mainPanel.add(getYesButton(), "growy, center, w 40%");
        mainPanel.setBackground(SHUTDOWN_BG_COLOR);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        return mainPanel;
    }

    private JLabel getMessage() {
        final JLabel message = new JLabel();
        message.setText(ResourceManager.getInstance().getString("ShutdownPanel.message"));
        message.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 36));
        return message;
    }

    private JButton getYesButton() {
        final JButton yesButton = new ColoredButton(ColorConstants.SHUTDOWN_YES_BTN);
        yesButton.setFont(new Font("Dialog", Font.BOLD, 36));
        yesButton.setAction(actionMap.get("confirmShutdownAction"));
        yesButton.setText(ResourceManager.getInstance().getString("ShutdownPanel.yes"));
        return yesButton;
    }

    private JButton getNoButton() {
        final JButton noButton = new ColoredButton(ColorConstants.SHUTDOWN_NO_BTN);
        noButton.setFont(new Font("Dialog", Font.BOLD, 36));
        noButton.setAction(actionMap.get("unconfirmShutDownAction"));
        noButton.setText(ResourceManager.getInstance().getString("ShutdownPanel.no"));
        return noButton;
    }

    @Override
    public void setVisible(final boolean isVisible) {
        owner.getGlassPane().setVisible(isVisible);
        super.setVisible(isVisible);
    }

    @org.jdesktop.application.Action(taskService = "RfidPalletTaskService")
    public void unconfirmShutDownAction() {
        this.setVisible(false);
    }

    @org.jdesktop.application.Action(taskService = "RfidPalletTaskService")
    public void confirmShutdownAction() {
        int timeBeforeSendingDisconnectionCommand;
        try {
            //check that RFIDConnect instance exist
            if(Utils.isNotNull(RFIDPalletApp.RFIDConnectJavaInstance)){
                //check that Pallet is in reading state or not (true or false)
                //change waiting time to send disconnection command accordingly to RFID reading state
                if(DeviceStatus.getIsReading()){
                    RFIDPalletApp.RFIDConnectJavaInstance.sendCommandThrows(ConnectCommandToSend.createCommand(CommandManager.COMMAND_ACTION.STOP_READ));
                }
                java.util.Timer disconnectionTimer = new java.util.Timer("exit-timer");
                disconnectionTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        RFIDPalletApp.RFIDConnectJavaInstance.sendCommand(ConnectCommandToSend.createCommand(CommandManager.COMMAND_ACTION.DISCONNECT_DEVICE));
                    }
                }, 250);
                disconnectionTimer.schedule(new DeconnectionRFIDConnectTask(disconnectionTimer), 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DeconnectionRFIDConnectTask extends TimerTask {

        private java.util.Timer timer = null;

        DeconnectionRFIDConnectTask(final java.util.Timer timer) {
            this.timer = timer;
        }

        @Override
        public void run() {
            try {
                System.out.println("Exit app");
                CJI.getInstance().close();
                RFIDPalletApp.getApplication().exitApplication();
            } catch (Exception e) {
                e.printStackTrace();
                this.timer.cancel();
            }
            this.timer.cancel();
        }
    }

}
