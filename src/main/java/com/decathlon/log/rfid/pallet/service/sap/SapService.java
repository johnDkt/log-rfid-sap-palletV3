package com.decathlon.log.rfid.pallet.service.sap;

import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.decathlon.log.rfid.sap.client.exception.SapClientException;

import java.util.List;

public interface SapService {

    List<TdoItem> retrieveContentFrom(final String searchId) throws SapClientException;

    <T> void sendHuWithContent(final T entityToSave) throws SapClientException;

}
