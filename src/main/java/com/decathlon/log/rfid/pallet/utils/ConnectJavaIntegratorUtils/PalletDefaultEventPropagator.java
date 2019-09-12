package com.decathlon.log.rfid.pallet.utils.ConnectJavaIntegratorUtils;

import com.decathlon.connectJavaIntegrator.tcp.listener.AbstractDefaultPropagatorListener;
import com.decathlon.connectJavaIntegrator.utils.ConnectCmdKey;
import org.apache.log4j.Logger;

/**
 * Created by Jonathan on 23/07/2018.
 */
public class PalletDefaultEventPropagator extends AbstractDefaultPropagatorListener {

    final static Logger logger = Logger.getLogger(PalletDefaultEventPropagator.class);

    public Boolean isConnected(Boolean pBoolValue) {
        if(pBoolValue){
            logger.debug("connect command succeed, "+ConnectCmdKey.deviceName+" connected");
        }else{
            logger.debug("connect command failed");
        }
        return pBoolValue;
    }

    public void getTags(String[] pTags) {
        /*int i = 0;
        List<String> tags = new ArrayList<String>();
        for (String tag : pTags){
            tags.add(tag);
            i++;
            //logger.debug("DefaultEventPropagator, tag n°" + i + " : " + tag);
        }
        */
    }

    public void error(String error) {
        logger.debug("error = " + error);
        /*
        String errorMessage = Utils.getOnlyStrings(error);
        if(!("").equals(errorMessage)){
            JOptionPane.showMessageDialog(RFIDPalletApp.getApplication().getMainView().getFrame(),
                    Utils.getOnlyStrings(error),
                    "RFID error",
                    JOptionPane.ERROR_MESSAGE);
        }
        */
    }
}
