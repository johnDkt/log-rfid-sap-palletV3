package com.decathlon.log.rfid.pallet.ui.panel;

import com.decathlon.log.rfid.pallet.resources.ResourceManager;
import com.decathlon.log.rfid.pallet.ui.button.ColoredButton;
import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import com.decathlon.log.rfid.pallet.constants.ColorConstants;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

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
        RFIDPalletApp.getApplication().exitApplication();
    }

}
