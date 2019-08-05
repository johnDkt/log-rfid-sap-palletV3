package com.decathlon.log.rfid.pallet.utils.ConnectJavaIntegratorUtils;

import com.decathlon.connectJavaIntegrator.utils.ConnectCmdKey;
import com.decathlon.connectJavaIntegrator.utils.Utils;
import com.decathlon.connectJavaIntegrator.utils.logger.DefaultDecorator;
import com.decathlon.connectJavaIntegrator.utils.logger.IDecorator;
import org.apache.log4j.Logger;


/**
 * Created by Jonathan on 14/09/2018.
 */
public class LoggerImpl implements IDecorator {


    public LoggerImpl() {
    }

    final static Logger LOGGER = Logger.getLogger(LoggerImpl.class);
    final static String ROOT_APP = ConnectCmdKey.CJI_NAME;
    String header = ROOT_APP+ DefaultDecorator.SEPARATOR;

    private String formatHeader(Class pClazz){
        return header + Utils.getTag(pClazz) + DefaultDecorator.CLASS_SEPARATOR;
    }

    public void print(Class pClazz, String pMessage){
        LOGGER.debug(formatHeader(pClazz)+pMessage);
    }

    public void print(Class pClazz, Throwable e){
        LOGGER.error(formatHeader(pClazz)+ e);
    }

    public void printError(Class pClazz, String pMessage){
        LOGGER.error(formatHeader(pClazz)+pMessage);
    }

    @Override
    public void printWarning(Class pClazz, String pMessage) {
        LOGGER.warn(formatHeader(pClazz)+pMessage);
    }

    private String constructLog(Class clazz,String msg){
        return header+clazz;
    }
}
