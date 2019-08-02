package com.decathlon.log.rfid.pallet.main;

import com.decathlon.log.rfid.pallet.ui.panel.ParamPanel;
import com.decathlon.log.rfid.pallet.ui.panel.ScanPanelLight;

import javax.swing.*;
import java.awt.*;

public class RFIDPalletMainPanel extends JPanel {

    private CardLayout cardLayout;

    private ScanPanelLight scanPanelLight;

    private ParamPanel paramPanel;

    public RFIDPalletMainPanel() {
        super();
        initialize();
    }

    private void initialize() {
        final Dimension screenSize = new Dimension(RFIDPalletApp.APPLICATION_WIDTH, RFIDPalletApp.APPLICATION_HEIGHT);
        setPreferredSize(screenSize);

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        add(getParamPanel(), PANEL_KEYS.PARAM_PANEL.toString());
        add(getScanPanelLight(), PANEL_KEYS.SCAN_LIGHT_PANEL.toString());

        showParamPanel();
    }

    public ScanPanelLight getScanPanelLight() {
        if (scanPanelLight == null) {
            scanPanelLight = new ScanPanelLight();
        }
        return scanPanelLight;
    }

    private ParamPanel getParamPanel() {
        if (paramPanel == null) {
            paramPanel = new ParamPanel();
        }
        return paramPanel;
    }

    private void switchViewTo(final PANEL_KEYS panelKey) {
        cardLayout.show(this, panelKey.toString());
    }

    public void showParamPanel() {
        switchViewTo(PANEL_KEYS.PARAM_PANEL);
        paramPanel.initFocus();
    }

    public void showScanLightPanel() {
        switchViewTo(PANEL_KEYS.SCAN_LIGHT_PANEL);
    }

    public void showParamPanelAndClearScanTextField() {
        showParamPanel();
        paramPanel.clearScanTextField();
    }

//    public void setQtyTotal(Integer total) {
//        scanPanelLight.setQtyTotal(total);
//    }
//
//    public void setQtyAndComputePercent(TdoItem item) {
//        scanPanelLight.setQtyDeprecated(item);
//    }

    enum PANEL_KEYS {

        PARAM_PANEL("paramPanel"),
        SCAN_LIGHT_PANEL("scanLightPanel");

        private String value;

        PANEL_KEYS(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

}
