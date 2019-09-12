package com.decathlon.log.rfid.pallet.ui.panel;

import com.decathlon.connectJavaIntegrator.factory.ConnectJavaIntegratorHelper;
import com.decathlon.connectJavaIntegrator.utils.Utils;
import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import com.decathlon.log.rfid.pallet.resources.ResourceManager;
import com.decathlon.log.rfid.pallet.ui.button.ColoredButton;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ConnectJavaCheckDialog extends JDialog {

    private static final Color SHUTDOWN_BG_COLOR = Color.WHITE;

    private ActionMap actionMap;

    private JFrame owner;

    private JPanel mainPanel;
    private JButton retryButton;


    public ConnectJavaCheckDialog(final JFrame owner) {
        super(owner);
        this.owner = owner;
        initialize();
    }

    private void initialize() {
        if (actionMap == null) {
            actionMap = RFIDPalletApp.getApplication().getContext().getActionMap(ConnectJavaCheckDialog.class, this);
        }

        setResizable(false);
        setUndecorated(true);
        setModal(true);
        setModalityType(ModalityType.APPLICATION_MODAL);

        setContentPane(createMainPanel());
    }



    private JPanel createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new MigLayout("ins 10, fill", "[50%][50%]", "[][30%!]"));
        mainPanel.add(getMessage(), "growy, span, center, w 85%");
        mainPanel.add(getExitButton(), "newline, growy, center, w 40%");
        mainPanel.add(createRetryButton(), "growy, center, w 40%");
        mainPanel.setBackground(SHUTDOWN_BG_COLOR);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        return mainPanel;
    }

    private JLabel getMessage() {
        final JLabel message = new JLabel();
        message.setText(ResourceManager.getInstance().getString("ConnectJavaCheckPanel.message"));
        message.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 36));
        return message;
    }

    // do nothing, just authorise to continue
    private JButton createRetryButton() {
        this.retryButton = new ColoredButton(Color.green);
        retryButton.setFont(new Font("Dialog", Font.BOLD, 36));
        retryButton.setAction(actionMap.get("retryConnectJavaCheckAction"));
        retryButton.setText(ResourceManager.getInstance().getString("ConnectJavaCheckPanel.retryCheck"));
        return retryButton;
    }

    // exit app
    private JButton getExitButton() {
        final JButton exitButton = new ColoredButton(Color.red);
        exitButton.setFont(new Font("Dialog", Font.BOLD, 36));
        exitButton.setAction(actionMap.get("confirmShutdownAction"));
        exitButton.setText(ResourceManager.getInstance().getString("ConnectJavaCheckPanel.exit"));
        return exitButton;
    }

    @Override
    public void setVisible(final boolean isVisible) {
        owner.getGlassPane().setVisible(isVisible);
        super.setVisible(isVisible);
    }

    public void checkRFIDConnectJavaStatus(){
        Boolean isRFIDConnectWorked = Utils.isNotNull(new ConnectJavaIntegratorHelper().returnInstance());
        if(isRFIDConnectWorked){
            this.setVisible(false);
        }
    }

    @org.jdesktop.application.Action(taskService = "RfidPalletTaskService")
    public void retryConnectJavaCheckAction() {
        retryButton.setEnabled(false);
        new Thread(new Runnable() {
            public void run() {
                mainPanel.updateUI();
                checkRFIDConnectJavaStatus();
                retryButton.setEnabled(true);
            }
        }).start();
    }

    @org.jdesktop.application.Action(taskService = "RfidPalletTaskService")
    public void confirmShutdownAction() {
        RFIDPalletApp.getApplication().exitApplication();
    }

}
