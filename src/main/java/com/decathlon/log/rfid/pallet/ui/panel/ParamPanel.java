package com.decathlon.log.rfid.pallet.ui.panel;

import com.decathlon.log.rfid.pallet.main.RFIDPalletSessionKeys;
import com.decathlon.log.rfid.pallet.resources.ResourceManager;
import com.decathlon.log.rfid.pallet.service.task.GetInformationFromWSTask;
import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.decathlon.log.rfid.pallet.ui.button.ColoredButton;
import com.decathlon.log.rfid.pallet.ui.field.EditableNumericalField;
import com.decathlon.log.rfid.pallet.constants.Constants;
import com.decathlon.log.rfid.pallet.tdo.TdoParameters;
import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import com.decathlon.log.rfid.pallet.constants.ColorConstants;
import com.decathlon.log.rfid.pallet.service.SessionService;
import com.decathlon.log.rfid.pallet.service.TaskManagerService;
import lombok.extern.log4j.Log4j;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Map;

@Log4j
public class ParamPanel extends JPanel {

    private static ActionMap actionMap;

    private JLabel titleLabel;
    private JButton quitButton;
    private JLabel truckLabel;
    private JLabel errorLabel;
    private EditableNumericalField searchIdTextField;
    private JButton validButton;

    private TaskManagerService taskManagerService;
    private SessionService sessionService;

    public ParamPanel() {
        super();
        taskManagerService = TaskManagerService.getInstance();
        sessionService = SessionService.getInstance();
        initialize();
    }

    private void initialize() {
        if (actionMap == null) {
            actionMap = RFIDPalletApp.getApplication().getContext().getActionMap(ParamPanel.class, this);
        }

        setLayout(new MigLayout("fill, ins 0, gap 0", "[20%!][]", "[50!][15%][20%][30%][]"));

        add(getQuitButton(), "grow");
        add(getTitleLabel(), "grow");
        add(getTruckLabel(), "newline, span, alignx center, aligny bottom");
        add(getSearchIdTextField(), "newline, span, center, w 50%, h 70!");
        add(getValidButton(), "newline, span, center, w 30%, h 120!");
        add(getErrorLabel(), "newline, span, center, growy, h 60!");
    }

    private JLabel getTitleLabel() {
        if (titleLabel == null) {
            titleLabel = new JLabel();
            titleLabel.setForeground(Color.white);
            titleLabel.setBackground(Color.darkGray);
            titleLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
            titleLabel.setOpaque(true);
        }
        return titleLabel;
    }

    private JLabel getTruckLabel() {
        if (truckLabel == null) {
            truckLabel = new JLabel();
            truckLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
            truckLabel.setText(ResourceManager.getInstance().getString("ParamPanel.scan.text"));
        }
        return truckLabel;
    }

    private JButton getQuitButton() {
        if (quitButton == null) {
            quitButton = new ColoredButton(ColorConstants.SCAN_QUIT_BTN_COLOR);
            quitButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
            quitButton.setAction(actionMap.get("quitApplicationAction"));
            quitButton.setText(ResourceManager.getInstance().getString("ParamPanel.quit.button.label"));
        }
        return quitButton;
    }

    private JTextField getSearchIdTextField() {
        if (searchIdTextField == null) {
            searchIdTextField = new EditableNumericalField(Constants.NUM_COLIS_LENGTH);
            searchIdTextField.setFont(new Font("Tahoma", Font.PLAIN, 36));
            searchIdTextField.setNextComponent(getValidButton());
            searchIdTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(final DocumentEvent e) {
                    handleChangingEditableTextField();
                }

                @Override
                public void removeUpdate(final DocumentEvent e) {
                    handleChangingEditableTextField();
                }

                @Override
                public void changedUpdate(final DocumentEvent e) {
                }
            });
        }
        return searchIdTextField;
    }

    private void handleChangingEditableTextField() {
        if (searchIdTextField.getText().length() == Constants.NUM_UAT_LENGTH ||
                searchIdTextField.getText().length() == Constants.NUM_UAT_LENGTH + 1 ||
                searchIdTextField.getText().length() == Constants.NUM_COLIS_LENGTH) {
            getValidButton().setEnabled(true);
        } else {
            getValidButton().setEnabled(false);
        }
    }

    private JLabel getErrorLabel() {
        if (errorLabel == null) {
            errorLabel = new JLabel();
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 36));
        }
        return errorLabel;
    }

    private JButton getValidButton() {
        if (validButton == null) {
            validButton = new ColoredButton(Color.GREEN);
            validButton.setFont(new Font("Tahoma", Font.BOLD, 25));
            validButton.setAction(actionMap.get("checkParams"));
            validButton.setText(ResourceManager.getInstance().getString("ParamPanel.valid.button.label"));
            validButton.setEnabled(false);
        }
        return validButton;
    }

    private TdoParameters getParameters() {
        final TdoParameters parameters = new TdoParameters();
        parameters.setSearchId(getSearchIdTextField().getText());
        return parameters;
    }

    public void initFocus() {
        getSearchIdTextField().requestFocusInWindow();
    }

    private void clearError() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getErrorLabel().setText("");
            }
        });
    }

    private void setError(final String error) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getErrorLabel().setText(error);
            }
        });
    }

    @Action(taskService = "RfidPalletTaskService")
    public void checkParams() throws Exception {
        clearError();

        final TdoParameters parameters = getParameters();
        final Map<String, String> errors = parameters.checkErrors();

        if (parameters.hasErrors(errors)) {
            setError(ResourceManager.getInstance().getString("CheckParameters.incorrect.data"));
        } else {
            final Task<Object, Void> task = new GetInformationFromWSTask() {
                @Override
                protected void succeeded(Object result) {
                    super.succeeded(result);
                    RFIDPalletApp.getView().getScanPanelLight().initUIWithDataFromServer(
                            sessionService.retrieveListFromSession(RFIDPalletSessionKeys.LISTE_ARTICLES_THEORIQUE,
                                    TdoItem.class));
                }
            };
            sessionService.storeParameters(parameters);
            taskManagerService.execute(task);

            RFIDPalletApp.getApplication().hideVirtualKeyBoard();
            RFIDPalletApp.getView().getScanPanelLight().startScanner();
            RFIDPalletApp.getView().getScanPanelLight().reinitUI();
            RFIDPalletApp.getView().showScanLightPanel();
        }
    }

    @Action(taskService = "RfidPalletTaskService")
    public void quitApplicationAction() {
        RFIDPalletApp.getApplication().showShutDownDialog();
    }

    public void clearScanTextField() {
        this.searchIdTextField.setText("");
    }
}
