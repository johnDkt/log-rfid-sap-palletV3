package com.decathlon.log.rfid.pallet.repository;

import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.decathlon.log.rfid.ws.client.WsParcelDetailsStub;
import com.decathlon.log.rfid.ws.server.*;
import com.decathlon.log.rfid.ws.server.GetColisDocument.GetColis;
import com.decathlon.log.rfid.ws.server.GetPalletDocument.GetPallet;
import com.decathlon.log.rfid.ws.server.SendPalletTagsDocument.SendPalletTags;
import com.decathlon.log.rfid.ws.server.SendPalletTagsResponseDocument.SendPalletTagsResponse;
import com.decathlon.log.rfid.ws.server.vo.xsd.VoAck;
import com.decathlon.log.rfid.ws.server.vo.xsd.VoPalletContent;
import com.decathlon.log.rfid.ws.server.vo.xsd.VoTruckItem;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;

@Log4j
public class BoExpedition extends BoAbstractWs {

    public BoExpedition() {
        super(WS_PARCEL_DETAILS_NAME);
    }

    public VoPalletContent getPalletContentFromWs(String searchId) throws Exception {
        log.info("getPalletContentFromWs Ws call with parameter id =" + searchId);

        try {

            WsParcelDetailsStub stub = new WsParcelDetailsStub(null, this.getUrl());
            init(stub);
            log.debug("getPallet Ws URL=" + this.getUrl());

            GetPalletDocument doc = GetPalletDocument.Factory.newInstance();
            GetPallet request = doc.addNewGetPallet();
            request.setUat(searchId);
            request.setLibrary(library);
            log.debug("GetPallet Ws Request=" + doc.toString());
            long start = System.currentTimeMillis();
            GetPalletResponseDocument response = stub.getPallet(doc);

            this.log("getPalletContent", System.currentTimeMillis() - start);
            log.debug("GetPalletContent Ws Response=" + response.toString());

            return response.getGetPalletResponse().getReturn();

        } catch (final Exception e) {
            log.error("WS Exception : ", e);
            throw e;
        }
    }

    public VoPalletContent getColisContentFromWs(String searchId) throws Exception {
        log.info("getColisContentFromWs Ws call with parameter id =" + searchId);

        try {

            WsParcelDetailsStub stub = new WsParcelDetailsStub(null, this.getUrl());
            init(stub);
            log.debug("getColis Ws URL=" + this.getUrl());

            GetColisDocument doc = GetColisDocument.Factory.newInstance();
            GetColis request = doc.addNewGetColis();
            request.setNumColis(searchId);
            request.setLibrary(library);
            log.debug("getColis Ws Request=" + doc.toString());
            long start = System.currentTimeMillis();
            GetColisResponseDocument response = stub.getColis(doc);

            this.log("getColisContent", System.currentTimeMillis() - start);
            log.debug("GetColisContent Ws Response=" + response.toString());

            return response.getGetColisResponse().getReturn();

        } catch (final Exception e) {
            log.error("WS Exception : ", e);
            throw e;
        }
    }


    public List<TdoItem> convertToItemList(VoPalletContent pallet) {
        if (pallet == null || !pallet.getFound()) {
            return new ArrayList<TdoItem>();
        } else {
            ArrayList<TdoItem> result = new ArrayList<TdoItem>();
            for (VoTruckItem vo : pallet.getItemsArray()) {
                TdoItem newItem = new TdoItem();
                result.add(newItem);

                newItem.setItem(vo.getItem());
                newItem.setQtyTheo(vo.getQty());
                newItem.setModel(vo.getModel());
                newItem.setFamily(vo.getFamily());
                newItem.setSize(vo.getSize());
                newItem.setEan(vo.getEan());

                for (String ean : vo.getEanUlItemArray()) {
                    newItem.addEncodedEan(ean);
                }

            }
            return result;
        }
    }


    public boolean saveDetailsForUat(List<String> tagsList, String uat, String mat) {

        boolean res = false;
        try {

            WsParcelDetailsStub stub = new WsParcelDetailsStub(null, this.getUrl());
            init(stub);

            String[] sgtins = new String[tagsList.size()];
            for (int i = 0; i < tagsList.size(); i++) {
                sgtins[i] = tagsList.get(i);
            }

            SendPalletTagsDocument doc = SendPalletTagsDocument.Factory.newInstance();
            SendPalletTags requestDoc = doc.addNewSendPalletTags();
            requestDoc.setUatNumber(uat);
            requestDoc.setMastName(mat);
            requestDoc.setLibrary(library);
            requestDoc.setSgtinArray(sgtins);

            log.debug("Request WS:" + requestDoc.toString());

            // call WS
            long start = System.currentTimeMillis();
            SendPalletTagsResponseDocument responseDoc = stub.sendPalletTags(doc);
            log("sendParcelTagsExp", System.currentTimeMillis() - start);
            SendPalletTagsResponse response = responseDoc.getSendPalletTagsResponse();
            VoAck voAck = response.getReturn();
            if (voAck != null) {
                res = voAck.getResult();
                log.debug("Response WS:" + voAck.toString());
            } else {
                log.debug("Response WS: null.");
            }
        } catch (Exception e) {
            log.error("WS error : ", e);
        }
        return res;
    }
}
