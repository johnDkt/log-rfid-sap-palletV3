package com.decathlon.log.rfid.pallet.main;

import com.decathlon.connectJavaIntegrator.configurator.CJIFluentConfigurator;
import com.decathlon.connectJavaIntegrator.mqtt.RFIDConnectJavaMqttInstance;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.CommandManager;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.sendToConnectJava.ConnectCommandToSend;
import com.decathlon.connectJavaIntegrator.utils.ConnectCmdKey;
import com.decathlon.connectJavaIntegrator.utils.Utils;
import com.decathlon.log.rfid.keyboard.bean.VirtualKeyBoardConfig;
import com.decathlon.log.rfid.keyboard.loader.VirtualKeyBoardLayoutType;
import com.decathlon.log.rfid.keyboard.ui.board.VirtualKeyBoard;
import com.decathlon.log.rfid.keyboard.ui.textfield.EditableTextField;
import com.decathlon.log.rfid.pallet.resources.ResourceManager;
import com.decathlon.log.rfid.pallet.service.SessionService;
import com.decathlon.log.rfid.pallet.ui.glassPane.DarkGlassPane;
import com.decathlon.log.rfid.pallet.ui.panel.ConnectJavaCheckDialog;
import com.decathlon.log.rfid.pallet.ui.panel.ShutdownJDialog;
import com.decathlon.log.rfid.pallet.utils.ConnectJavaIntegratorUtils.LoggerImpl;
import com.decathlon.log.rfid.pallet.utils.RFIDProperties;
import lombok.extern.log4j.Log4j;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.TaskService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.Executors;


@Log4j
public class RFIDPalletApp extends SingleFrameApplication {
    private static final Logger LOGGER = Logger.getLogger(RFIDPalletApp.class);
    public static final int APPLICATION_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int APPLICATION_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final String TASK_SERVICE_NAME = "RfidPalletTaskService";
    private static RFIDPalletMainPanel view;
    public static RFIDConnectJavaMqttInstance RFIDConnectJavaInstance;

    private ShutdownJDialog shutDownJDialog;
    private ConnectJavaCheckDialog connectJavaCheckDialog;
    private VirtualKeyBoard virtualKeyBoard;

    private DarkGlassPane darkGlassPane;


    public static RFIDPalletMainPanel getView() {
        if (view == null) {
            view = new RFIDPalletMainPanel();
        }
        return view;
    }

    /**
     * A convenient static getter for the application instance.
     *
     * @return the instance of RFIDToolApp
     */
    public static RFIDPalletApp getApplication() {
        return Application.getInstance(RFIDPalletApp.class);
    }

    /**
     * Main method launching the application.
     *
     * @param args program args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final Properties log = new Properties();
        String propertiesFileName = "log4j-pallet.properties";
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(propertiesFileName);
        if(null != inputStream){
            log.load(inputStream);
            PropertyConfigurator.configure(log);
            launch(RFIDPalletApp.class, args);
        }else{
            LOGGER.error("impossible to read "+propertiesFileName);
        }
    }

    @Override
    protected void startup() {

        log.warn("Screen size = " + APPLICATION_WIDTH + " x " + APPLICATION_HEIGHT);

        initTaskService();
        initConnectJava();
        initResourceManager();

        final JFrame jFrame = getMainFrame();
        jFrame.setResizable(false);
        jFrame.setAlwaysOnTop(true);
        jFrame.setUndecorated(true);
        jFrame.setLocationByPlatform(true);
        jFrame.setUndecorated(Utils.isInDevMode() ? false : true);

        jFrame.setGlassPane(getDarkGlassPane());

        final Container mainContainer = jFrame.getContentPane();
        mainContainer.setLayout(new MigLayout("fill, ins 0, gap 0, hidemode 2", "", "[][]"));
        mainContainer.add(getView(), "grow");
        mainContainer.add(getVirtualKeyBoard(), "newline, growx, h 33%::");

        hideVirtualKeyBoard();

        jFrame.pack();
        //GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].setFullScreenWindow(jFrame);
        show(jFrame);

        getView().showParamPanel();
    }

    private ShutdownJDialog getShutDownJDialog() {
        if (shutDownJDialog == null) {
            shutDownJDialog = new ShutdownJDialog(getMainFrame());
            shutDownJDialog.setSize(700, 350);
            shutDownJDialog.setLocationRelativeTo(null);
        }
        return shutDownJDialog;
    }

    private ConnectJavaCheckDialog getConnectJavaCheckJDialog() {
        if (connectJavaCheckDialog == null) {
            connectJavaCheckDialog = new ConnectJavaCheckDialog(getMainFrame());
            connectJavaCheckDialog.setSize(900, 600);
            connectJavaCheckDialog.setLocationRelativeTo(null);
        }
        return connectJavaCheckDialog;
    }

    private DarkGlassPane getDarkGlassPane() {
        if (darkGlassPane == null) {
            darkGlassPane = new DarkGlassPane();
            darkGlassPane.setSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT));
        }
        return darkGlassPane;
    }

    private void initTaskService() {
        getContext().removeTaskService(getContext().getTaskService());
        getContext().addTaskService(new TaskService(TASK_SERVICE_NAME, Executors.newCachedThreadPool(Executors.defaultThreadFactory())));

        if (log.isDebugEnabled()) {
            for (TaskService service : getContext().getTaskServices()) {
                log.debug("Registered task service : " + service.getName());
            }
        }
    }

    private void initConnectJava() {
        LOGGER.info(System.getProperty("user.dir"));
        SessionService.getInstance().storeInSession(RFIDPalletSessionKeys.SESSION_RFID_READING_STATE, false);
        this.RFIDConnectJavaInstance = new CJIFluentConfigurator(ConnectCmdKey.OBSERVABLE_PATTERN,new LoggerImpl()).returnInstance();

        if(Utils.isNotNull(RFIDConnectJavaInstance)){
            RFIDConnectJavaInstance.sendCommand(ConnectCommandToSend.createCommand(CommandManager.COMMAND_ACTION.CONNECT_DEVICE));
        }else{
            LOGGER.error("RFIDConnect instance is null");
            showConnectJavaCheckDialog();
        }
    }


    private void initResourceManager() {
        ResourceManager.getInstance(new Locale(RFIDProperties.getValue(RFIDProperties.PROPERTIES.LANGUAGE)));
    }

    public void exitApplication() {
        this.exit();
    }

    public void showShutDownDialog() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getShutDownJDialog().setVisible(true);
            }
        });
    }

    public void showConnectJavaCheckDialog() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getConnectJavaCheckJDialog().setVisible(true);
            }
        });
    }

    public VirtualKeyBoard getVirtualKeyBoard() {
        if (virtualKeyBoard == null) {
            final VirtualKeyBoardConfig virtualKeyBoardConfig = VirtualKeyBoardConfig.builder()
                    .virtualKeyBoardLayoutType(getKeyBoardLayoutType())
                    .spaceTextButton(ResourceManager.getInstance().getString("VirtualKeyBoard.space.button"))
                    .enterTextButton(ResourceManager.getInstance().getString("VirtualKeyBoard.enter.button"))
                    .build();
            virtualKeyBoard = new VirtualKeyBoard(virtualKeyBoardConfig);
            virtualKeyBoard.setBackground(Color.DARK_GRAY);
        }
        return virtualKeyBoard;
    }

    private VirtualKeyBoardLayoutType getKeyBoardLayoutType() {
        final String locale = RFIDProperties.getValue(RFIDProperties.PROPERTIES.LANGUAGE);
        if ("de".equalsIgnoreCase(locale)) {
            return VirtualKeyBoardLayoutType.QWERTZ;
        } else if ("fr".equalsIgnoreCase(locale)) {
            return VirtualKeyBoardLayoutType.AZERTY;
        } else {
            return VirtualKeyBoardLayoutType.QWERTY;
        }
    }

    public void showVirtualKeyBoard(final EditableTextField target) {
        getVirtualKeyBoard().setEditableTextField(target);
        getVirtualKeyBoard().setVisible(true);
    }

    public void hideVirtualKeyBoard() {
        getVirtualKeyBoard().setVisible(false);
    }

    /**
     * @see org.jdesktop.application.SingleFrameApplication#shutdown()
     */
    @Override
    protected void shutdown() {
        super.shutdown();

    }

    public void displayErrorDialog(){
        JOptionPane.showMessageDialog(getMainFrame(),
                ResourceManager.getInstance().getString("app.shutdown.error"),
                ResourceManager.getInstance().getString("app.shutdown.error.title"),
                JOptionPane.ERROR_MESSAGE);
    }




    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * @see org.jdesktop.application.Application#initialize(java.lang.String[])
     */
    @Override
    protected void initialize(String[] args) {
        super.initialize(args);
        System.setProperty("java.net.useSystemProxies", RFIDProperties.getValue(RFIDProperties.PROPERTIES.USE_SYSTEM_PROXIES));
    }
}
