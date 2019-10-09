package com.decathlon.log.rfid.pallet.scan.reader;

import com.decathlon.connectJavaIntegrator.configurator.CJIFluentConfigurator;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.commonCommandsObject.deviceStatus.DeviceStatus;
import com.decathlon.connectJavaIntegrator.mqtt.handleCommands.receiveFromConnectJava.EventPropagatorObject;
import com.decathlon.connectJavaIntegrator.utils.ConnectCmdKey;
import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.embisphere.esr.utils.epc.EPCUtils;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;

import java.util.*;

@Log4j
public class TagsListener implements Observer {

    private static final Logger SGTIN_LOG = Logger.getLogger("sgtin");
    private static final Logger SGTIN_OK_LOG = Logger.getLogger("sgtinok");
    private static final Logger LOGGER = Logger.getLogger(TagsListener.class);

    @Getter
    private List<String> scannedTags;

    @Getter
    private TagsHandler tagsHandler;


    public TagsListener(final TagsHandler tagsHandler) {
        this.scannedTags = new ArrayList<String>();
        this.tagsHandler = tagsHandler;
        CJIFluentConfigurator.AddObserverStatic(this);
    }

    public void clearTags() {
        this.scannedTags.clear();
    }

    public void getTags(String[] tags){
        log.info("Listener received " + tags.length + " tags");
        for (String tag : tags){
            SGTIN_LOG.debug(tag + "|" + "unknown antenna");
            if(!scannedTags.contains(tag)){
                scannedTags.add(tag);
                sendTagToTagsHandler(tag);
            }
        }
    }

    public void error(String error) {
        LOGGER.debug("error = " + error);
    }

    private void sendTagToTagsHandler(final String sgtinNumber) {
        try {
            final TdoItem tdoItem = mapTDoItem(sgtinNumber);

            if (checkEan(tdoItem.getEan())) {
                throw new Exception("Ean is zero for epc " + tdoItem.getSgtin());
            } /*else if (scanner.isErrorTag(tdoItem.getSgtin())) {
                throw new Exception(tdoItem.getSgtin() + " is an error tag (see scanner.properties file)");
            }*/
            SGTIN_OK_LOG.debug(tdoItem.getSgtin() + "|" + tdoItem.getGtin14() + "|"
                    + tdoItem.getEan());

            tagsHandler.handleTagRead(tdoItem);
        } catch (final Exception e) {
            log.error("Exceptioon while handling tags", e);
        }
    }

    private TdoItem mapTDoItem(final String sgtinNumber) throws Exception {
        final TdoItem tdoItem = new TdoItem();
        tdoItem.setSgtin(sgtinNumber);
        tdoItem.setEan(EPCUtils.getGTIN13(sgtinNumber));
        //tdoItem.setGtin14();
        return tdoItem;
    }

    private boolean checkEan(final String ean) {
        return ean != null && Long.parseLong(ean) == 0;
    }


    @Override
    public void update(Observable o, Object arg) {
        LOGGER.info(DeviceStatus.display());
        System.out.println("update call");
        EventPropagatorObject event = (EventPropagatorObject) arg;
        LOGGER.info(event.toString());
        if(ConnectCmdKey.ACTION_START.equals(event.getTitleEvent())){
           for(Object tags : event.getFunctionnalDataContainer().get(EventPropagatorObject.TAG)){
               if(tags instanceof String[]){
                   this.getTags((String[]) tags);
               }

           }
            //this.getTags(event.getFunctionnalDataContainer().get(EventPropagatorObject.TAG));
        }else if("clearTags".equals(event.getTitleEvent())){
            this.clearTags();
        }
    }
}
