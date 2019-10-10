package com.decathlon.log.rfid.pallet.ui.panel;

import com.decathlon.connectJavaIntegrator.factory.CJI;
import com.decathlon.connectJavaIntegrator.mqtt.RFIDConnectJavaMqttInstance;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.CommandManager;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.commonCommandsObject.deviceStatus.DeviceStatus;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.receiveFromConnectJava.EventPropagatorObject;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.receiveFromConnectJava.IRFIDConnectCmdReceiverListener;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.receiveFromConnectJava.observable.ConnectCommandObservable;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.sendToConnectJava.ConnectCommandToSend;
import com.decathlon.connectJavaIntegrator.utils.ConnectCmdKey;
import com.decathlon.connectJavaIntegrator.utils.Utils;
import com.decathlon.log.rfid.pallet.constants.ColorConstants;
import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import com.decathlon.log.rfid.pallet.main.RFIDPalletSessionKeys;
import com.decathlon.log.rfid.pallet.resources.ResourceManager;
import com.decathlon.log.rfid.pallet.scan.reader.TagsHandler;
import com.decathlon.log.rfid.pallet.scan.reader.TagsListener;
import com.decathlon.log.rfid.pallet.scan.task.PlayTask;
import com.decathlon.log.rfid.pallet.scan.task.SaveDetailsTask;
import com.decathlon.log.rfid.pallet.scan.task.StartPanelCommandButtonsActionTask;
import com.decathlon.log.rfid.pallet.service.SessionService;
import com.decathlon.log.rfid.pallet.service.TaskManagerService;
import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.decathlon.log.rfid.pallet.tdo.TdoParameters;
import com.decathlon.log.rfid.pallet.ui.button.ColoredButton;
import com.decathlon.log.rfid.pallet.ui.scan.ItemProgressPanel;
import com.decathlon.log.rfid.pallet.ui.scan.ItemTableModel;
import com.decathlon.log.rfid.pallet.ui.scan.ItemsScrollPane;
import com.decathlon.log.rfid.pallet.ui.scan.ItemsTable;
import com.decathlon.log.rfid.pallet.utils.RFIDProperties;
import lombok.extern.log4j.Log4j;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Log4j
public class ScanPanelLight extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(ScanPanelLight.class);

    private static ActionMap actionMap;

    private JLabel summaryLabel;
    private JButton quitButton;
    private ColoredButton resetButton;
    private ColoredButton validButton;
    private ColoredButton detailsButton;
    private JLabel countTagsLabel;

    private boolean details = false;

    private TagsHandler tagsHandler;
    private TagsListener tagsListener;

    private JPanel panelDetails;
    private JLabel readingLabel;

    private ItemsScrollPane allItems;

    private JLabel countTagsValueLabel;
    private ItemProgressPanel countTagsProgressBar;
    private JLabel expectedTagsValueLabel;

    private Timer timerReadPallet;
    private JLabel playLabel;
    private PlayTask playTask;

    private Timer autoValidationTimer;

    private TaskManagerService taskManagerService;
    private SessionService sessionService;
    private RFIDConnectJavaMqttInstance RFIDConnectInstance;

    public ScanPanelLight() {
        super();
        this.setSize(1024,768);
        this.sessionService = SessionService.getInstance();
        try {
            this.RFIDConnectInstance = CJI.getInstance();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        this.taskManagerService = TaskManagerService.getInstance();
        initTagsListenerAndTagsHandler();
        initialize();
    }

    /**
     * This method initializes this
     */
    private void initialize() {

        if (actionMap == null) {
            actionMap = RFIDPalletApp.getApplication().getContext()
                    .getActionMap(ScanPanelLight.class, this);
        }

        final JPanel topPanel = new JPanel(new MigLayout("fill, ins 0, gap 0", "[75][][75]", ""));
        topPanel.add(getQuitButton(), "grow");
        topPanel.add(getSummaryLabel(), "grow");
        topPanel.add(getDetailsButton(), "grow");

        final JPanel playPanel = new JPanel(new MigLayout("fill, ins 0, gap 0", "[center]", "[][20%!]"));
        playPanel.add(getPlayLabel());
        playPanel.add(getReadingLabel(), "newline, grow, center");

        final JPanel buttonsPanel = new JPanel(new MigLayout("fill, ins 0, gap 0,", "[][100]", ""));
        buttonsPanel.add(getResetButton(), "w 200, h 50%, right, split 2");
        buttonsPanel.add(getValidButton(), "w 300, h 75%, gapleft 100, gapright 100");
        buttonsPanel.add(playPanel, "grow");

        final JPanel countingPanel = new JPanel(new MigLayout("fill, ins 0, gap 0", "[:30%:]10![20!]10![:30%:]", ""));
        countingPanel.add(getCountTagsValueLabel(), "right");
        countingPanel.add(getCountTagsLabel(), "center");
        countingPanel.add(getExpectedTagsValueLabel(), "left");

        setLayout(new MigLayout("fill, ins 0, gap 0", "", "[50!][30%][][50!][]"));
        add(topPanel, "grow");
        add(buttonsPanel, "newline, grow");
        add(countingPanel, "newline, grow");
        add(getGlobalProgressBar(), "newline, grow");
        add(getPanelDetails(), "newline, grow");
    }

    private void initTagsListenerAndTagsHandler() {
        tagsHandler = new TagsHandler() {
            @Override
            public void updateUIWhenTagRead(final TdoItem tdoItem) {
                final ItemsTable table = allItems.getItemsTable();
                table.addData(tdoItem);

                int expected = table.getExpectedTagCount();
                int read = table.getReadTagCount();
                updateQtyUI(read, expected);
            }
        };

        tagsListener = new TagsListener(tagsHandler);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            final String uat = sessionService.retrieveFromSession(RFIDPalletSessionKeys.SESSION_PARAMETERS_KEY,
                    TdoParameters.class).getSearchId();

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setTitle(uat);
                }
            });
        }
    }

    private JLabel getSummaryLabel() {
        if (summaryLabel == null) {
            summaryLabel = new JLabel();
            summaryLabel.setHorizontalAlignment(SwingConstants.CENTER);
            summaryLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            summaryLabel.setBackground(ColorConstants.TITLE_BG_COLOR);
            summaryLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
            summaryLabel.setOpaque(true);
            summaryLabel.setForeground(Color.white);
        }
        return summaryLabel;
    }

    private JLabel getCountTagsLabel() {
        if (countTagsLabel == null) {
            countTagsLabel = new JLabel();
            countTagsLabel.setFont(new Font("Dialog", Font.BOLD, 65));
            countTagsLabel.setText("/");
        }
        return countTagsLabel;
    }

    private JLabel getCountTagsValueLabel() {
        if (countTagsValueLabel == null) {
            countTagsValueLabel = new JLabel();
            countTagsValueLabel.setText("999 999");
            countTagsValueLabel.setFont(new Font("Dialog", Font.BOLD, 65));
        }
        return countTagsValueLabel;
    }

    private ColoredButton getDetailsButton() {
        if (detailsButton == null) {
            detailsButton = new ColoredButton(Color.LIGHT_GRAY);
            detailsButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
            detailsButton.setAction(actionMap.get("OpenDetailsPanel"));
            detailsButton.setText(ResourceManager.getInstance().getString(
                    "ScanPanelLight.details.button"));
            detailsButton.setVisible(Boolean.parseBoolean(RFIDProperties
                    .getValue(RFIDProperties.PROPERTIES.DEBUG)));
        }
        return detailsButton;
    }

    private ColoredButton getResetButton() {
        if (resetButton == null) {
            resetButton = new ColoredButton(Color.RED);
            resetButton.setFont(new Font("Tahoma", Font.BOLD, 25));
            resetButton.setAction(actionMap.get("reset"));
            resetButton.setText(ResourceManager.getInstance().getString(
                    "ScanPanelLight.resetButton.text"));
        }
        return resetButton;
    }

    private JPanel getPanelDetails() {
        if (panelDetails == null) {
            panelDetails = new JPanel();
            panelDetails.setLayout(new MigLayout("fill"));
            panelDetails.add(getAllItemsPanel(), "grow");
        }
        return panelDetails;
    }

    private JLabel getReadingLabel() {
        if (readingLabel == null) {
            readingLabel = new JLabel();
            readingLabel.setText(ResourceManager.getInstance().getString(
                    "ScanPanelLight.readingLabel.text"));
            readingLabel.setFont(new Font("Dialog", Font.BOLD, 25));
            readingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return readingLabel;
    }

    private JLabel getPlayLabel() {
        if (playLabel == null) {
            try {
                final BufferedImage myPicture = ImageIO.read(ClassLoader.getSystemResource("media_play.png"));
                playLabel = new JLabel(new ImageIcon(myPicture));
                playLabel.setVisible(false);
            } catch (final IOException e) {
                log.error(ResourceManager.getInstance().getString("ScanPanelLight.media.unload"), e);
            }
        }
        return playLabel;
    }

    /**
     * This method initializes global progress bar
     *
     * @return javax.swing.JButton
     */
    public ItemProgressPanel getGlobalProgressBar() {
        if (countTagsProgressBar == null) {
            countTagsProgressBar = new ItemProgressPanel();
            countTagsProgressBar.setBackground(new Color(201, 228, 255));
            countTagsProgressBar.setOpaque(true);
            countTagsProgressBar.setBorder(BorderFactory
                    .createLineBorder(Color.BLACK));
            countTagsProgressBar.setQtyAndComputePercent(0);
        }
        return countTagsProgressBar;
    }

    public void togglePanelDetails() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (details) {
                    detailsButton.setText(ResourceManager.getInstance().getString(
                            "ScanPanelLight.hidedetails.button"));
                } else {
                    detailsButton.setText(ResourceManager.getInstance().getString(
                            "ScanPanelLight.details.button"));
                }
                panelDetails.setVisible(!details);
            }
        });
        details = !details;
    }

    /**
     * This method initializes quitButton
     *
     * @return javax.swing.JButton
     */
    public JButton getQuitButton() {
        if (quitButton == null) {
            quitButton = new ColoredButton(
                    ColorConstants.SCAN_QUIT_BTN_COLOR);
            quitButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
            quitButton.setAction(actionMap.get("quitApplicationAction"));
            quitButton.setText(ResourceManager.getInstance().getString(
                    "ScanPanel.quit.button.label"));
        }
        return quitButton;
    }

    /**
     * This method initializes valid button
     *
     * @return javax.swing.JButton
     */
    private JButton getValidButton() {
        if (validButton == null) {
            validButton = new ColoredButton(
                    ColorConstants.START_SCAN_BTN_COLOR);
            validButton.setFont(new Font("Tahoma", Font.BOLD, 25));
            validButton.setAction(actionMap.get("validPallet"));
            validButton.setText(ResourceManager.getInstance().getString(
                    "ScanPanelLight.validButton.text"));
        }
        return validButton;
    }

    private ItemsScrollPane getAllItemsPanel() {
        if (allItems == null) {
            allItems = new ItemsScrollPane();
        }
        return allItems;
    }

    private JLabel getExpectedTagsValueLabel() {
        if (expectedTagsValueLabel == null) {
            expectedTagsValueLabel = new JLabel();
            expectedTagsValueLabel.setText("0");
            expectedTagsValueLabel
                    .setHorizontalTextPosition(SwingConstants.LEFT);
            expectedTagsValueLabel.setHorizontalAlignment(SwingConstants.LEFT);
            expectedTagsValueLabel.setFont(new Font("Dialog", Font.BOLD, 65));
        }
        return expectedTagsValueLabel;
    }

    @Action(taskService = "RfidPalletTaskService")
    public void quitApplicationAction() {
        Logger.getLogger("action").info(sessionService.retrieveFromSession(RFIDPalletSessionKeys.SESSION_PARAMETERS_KEY, TdoParameters.class).getSearchId() + "|QUIT");
        RFIDPalletApp.getApplication().showShutDownDialog();
    }

    @Action(taskService = "RfidPalletTaskService")
    public void OpenDetailsPanel() {
        togglePanelDetails();
    }

    public void setTitle(final String title) {
        getSummaryLabel().setText("UAT num " + title);
    }

    @Action(taskService = "RfidPalletTaskService")
    public void reset() {
        Logger.getLogger("action").info(sessionService.retrieveFromSession(RFIDPalletSessionKeys.SESSION_PARAMETERS_KEY, TdoParameters.class).getSearchId() + "|RESET");
        stopReading();
        ((ItemTableModel)(this.getAllItemsPanel().getItemsTable().getModel())).getExpectedAndDisplayedItems().setDataFromTheServer(false);
        goToParamPanelWithSendDataToServer(false);
    }

    @Action(taskService = "RfidPalletTaskService")
    public void validPallet() {
        Logger.getLogger("action").info(sessionService.retrieveFromSession(RFIDPalletSessionKeys.SESSION_PARAMETERS_KEY, TdoParameters.class).getSearchId() + "|VALID");
        stopReading();
        ((ItemTableModel)(this.getAllItemsPanel().getItemsTable().getModel())).getExpectedAndDisplayedItems().setDataFromTheServer(false);
        goToParamPanelWithSendDataToServer(true);
    }

    public void initUIWithDataFromServer(final List<TdoItem> theoreticalTags) {
        final int itemsQuantity = theoreticalTags.size();

        if (itemsQuantity == 0) {
            return;
        }

        final ItemsTable itemsTable = this.allItems.getItemsTable();
        itemsTable.setTheoreticalTags(theoreticalTags); //theoreticalTags = la liste des tdo items récup du WS(GE)
        final int expected = itemsTable.getExpectedTagCount();
        final int read = itemsTable.getReadTagCount();
        updateQtyUI(read, expected);
    }

    public void startScanner() {
        log.debug("Clean table");
        final ItemsTable itemsTable = allItems.getItemsTable();
        itemsTable.clear();

        log.debug("startStopScanner - Begin");

        this.playTask = new PlayTask(getPlayLabel());
        IRFIDConnectCmdReceiverListener test =  RFIDConnectInstance.getClassThatComputeReceivedCommands();
        if(ConnectCmdKey.OBSERVABLE_PATTERN.equals(CJI.getInstanceType())){
            ((ConnectCommandObservable) test).propagateEvent(new EventPropagatorObject(this.getClass().getSimpleName(),"clearTags"));
        }

        //TagsListener clientListenerImpl = (TagsListener) CJI.getListenerfrom(TagsListener.class);
        //clientListenerImpl.clearTags();
        startReading();

        timerReadPallet = new Timer("Timer-Read-Pallet");
        timerReadPallet.schedule(new StopScannerTask(timerReadPallet),
                Long.parseLong(RFIDProperties
                        .getValue(RFIDProperties.PROPERTIES.READ_PALLET_TIMEOUT)));

        taskManagerService.execute(new StartPanelCommandButtonsActionTask(
                Integer.parseInt(RFIDProperties
                        .getValue(RFIDProperties.PROPERTIES.READ_TIMEOUT))));
    }


    private void updateQtyUI(final int readCount, final int expectedCount) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getGlobalProgressBar().setTotal(expectedCount);
                getGlobalProgressBar().setQtyAndComputePercent(readCount);
                getExpectedTagsValueLabel().setText(String.valueOf(expectedCount));
                getCountTagsValueLabel().setText(String.valueOf(readCount));
            }
        });
    }

    private void stopScanner() {
        stopReading();

        if (Boolean.parseBoolean(RFIDProperties.getValue(RFIDProperties.PROPERTIES.AUTO_VALIDATION))) {

            final Integer autoValidationTimeout =
                    Integer.parseInt(RFIDProperties.getValue(RFIDProperties.PROPERTIES.AUTO_VALIDATION_TIMEOUT));

            final int autoValidationLogInterval = 5000;

            autoValidationTimer = new Timer("Timer-auto-validation");
            final TimerTask autoValidationLogTimerTask = new TimerTask() {

                private int numberOfCalls = 0;

                @Override
                public void run() {
                    long timeLeft = autoValidationTimeout - (numberOfCalls * autoValidationLogInterval);
                    ++numberOfCalls;
                    log.debug("Auto validation in " + (timeLeft / 1000) + " seconds");
                }

            };

            final TimerTask autoValidationSendToServerTimerTask = new TimerTask() {
                @Override
                public void run() {
                    autoValidationLogTimerTask.cancel();
                    log.debug("Auto validation send tags to server");
                    taskManagerService.blockUIThenExecuteTask(new SaveDetailsTask(tagsListener.getScannedTags(),
                            sessionService.retrieveFromSession(RFIDPalletSessionKeys.SESSION_PARAMETERS_KEY, TdoParameters.class).getSearchId()));
                    RFIDPalletApp.getView().showParamPanelAndClearScanTextField();
                }
            };

            autoValidationTimer.schedule(autoValidationLogTimerTask, 0, autoValidationLogInterval);
            autoValidationTimer.schedule(autoValidationSendToServerTimerTask, autoValidationTimeout);
        }
    }

    private void stopAutoValidation() {
        if (autoValidationTimer != null) {
            autoValidationTimer.cancel();
            log.debug("Auto validation cancelled !");
        }
    }

    private void stopReading() {
        this.playTask.stop();
        this.timerReadPallet.cancel();

        if(Utils.isNotNull(RFIDPalletApp.RFIDConnectJavaInstance)){
            if(DeviceStatus.getIsReading()){
                try {
                    RFIDPalletApp.RFIDConnectJavaInstance.sendCommandThrows(ConnectCommandToSend.createCommand(CommandManager.COMMAND_ACTION.STOP_READ));
                } catch (Exception e) {
                    log.error(e);
                }
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getReadingLabel().setVisible(false);
            }
        });
    }

    private void startReading() {
        this.playTask.start();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getReadingLabel().setVisible(true);
            }
        });
    }

    private void goToParamPanelWithSendDataToServer(final boolean isSendToServer) {
        stopAutoValidation();
        if (isSendToServer && hasTagsToSend()) {
            taskManagerService.blockUIThenExecuteTask(new SaveDetailsTask(tagsListener.getScannedTags(),
                    sessionService.retrieveFromSession(RFIDPalletSessionKeys.SESSION_PARAMETERS_KEY, TdoParameters.class).getSearchId()));
            RFIDPalletApp.getView().showParamPanelAndClearScanTextField();

        }
        RFIDPalletApp.getView().showParamPanel();
    }

    private boolean hasTagsToSend() {
        return tagsListener.getScannedTags().size() >0;
    }

    public void reinitUI() {
        this.countTagsValueLabel.setText("0");
        this.expectedTagsValueLabel.setText("0");
        ((ItemTableModel)(this.getAllItemsPanel().getItemsTable().getModel())).clear();
        this.countTagsProgressBar.reset();

    }

    private class StopScannerTask extends TimerTask {

        private Timer timer = null;

        StopScannerTask(final Timer timer) {
            this.timer = timer;
        }

        @Override
        public void run() {
            stopScanner();
            this.timer.cancel();
        }

    }

}
