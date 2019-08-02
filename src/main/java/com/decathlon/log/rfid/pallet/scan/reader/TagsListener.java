package com.decathlon.log.rfid.pallet.scan.reader;

import EmbiReader.SGTINBean;
import com.decathlon.log.rfid.epc.decoder.EPCDecoderException;
import com.decathlon.log.rfid.pallet.service.SgtinService;
import com.decathlon.log.rfid.pallet.tdo.TdoItem;
import com.decathlon.log.rfid.reader.bo.BoScanner;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class TagsListener implements PropertyChangeListener {

    private static final Logger SGTIN_LOG = Logger.getLogger("sgtin");
    private static final Logger SGTIN_OK_LOG = Logger.getLogger("sgtinok");

    private SgtinService sgtinService;

    @Getter
    private List<String> scannedTags;

    @Getter
    private TagsHandler tagsHandler;
    @Setter
    private BoScanner scanner;

    public TagsListener(final TagsHandler tagsHandler) {
        this.scannedTags = new ArrayList<String>();
        this.sgtinService = SgtinService.getInstance();
        this.tagsHandler = tagsHandler;
    }

    public void clearTags() {
        this.scannedTags.clear();
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent evt) {
        if (BoScanner.LIST_PROPERTY_NAME.equals(evt.getPropertyName())) {
            final List<SGTINBean> tmpListTags = mapToSGTINBean(evt.getNewValue());
            log.info("Listener received " + tmpListTags.size() + " tags");

            for (final SGTINBean bean : tmpListTags) {
                final String sgtinNumber = sgtinService.getSgtinGetter().getSgtinId(bean);
                final String antenna = sgtinService.getSgtinGetter().getAntenna(bean);

                SGTIN_LOG.debug(sgtinNumber + "|" + antenna);

                if (!scannedTags.contains(sgtinNumber)) {
                    scannedTags.add(sgtinNumber);
                    sendTagToTagsHandler(sgtinNumber);
                }
            }

        }
    }

    private List<SGTINBean> mapToSGTINBean(final Object object) {
        final List<Object> list = (List<Object>)object;
        return Lists.transform(list, new Function<Object, SGTINBean>() {
            @Override
            public SGTINBean apply(final Object o) {
                return SGTINBean.class.cast(o);
            }
        });
    }

    private void sendTagToTagsHandler(final String sgtinNumber) {
        try {
            final TdoItem tdoItem = mapTDoItem(sgtinNumber);

            if (checkEan(tdoItem.getEan())) {
                throw new Exception("Ean is zero for epc " + tdoItem.getSgtin());
            } else if (scanner.isErrorTag(tdoItem.getSgtin())) {
                throw new Exception(tdoItem.getSgtin() + " is an error tag (see scanner.properties file)");
            }
            SGTIN_OK_LOG.debug(tdoItem.getSgtin() + "|" + tdoItem.getGtin14() + "|"
                    + tdoItem.getEan());

            tagsHandler.handleTagRead(tdoItem);
        } catch (final EPCDecoderException e) {
            log.error(e);
            final TdoItem tdoItem = new TdoItem();
            tdoItem.setSgtin(sgtinNumber);
            tagsHandler.handleTagRead(tdoItem);
        } catch (final Exception e) {
            log.error("Exceptioon while handling tags", e);
        }




    }

    private TdoItem mapTDoItem(final String sgtinNumber) throws Exception {
        final Object epc = sgtinService.getSgtinDecoder().getEPC(sgtinNumber);
        final TdoItem tdoItem = new TdoItem();
        tdoItem.setSgtin(sgtinNumber);
        tdoItem.setEan(sgtinService.getSgtinDecoder().getEan13(epc));
        tdoItem.setGtin14(sgtinService.getSgtinDecoder().getGtin14(epc));
        return tdoItem;
    }

    private boolean checkEan(final String ean) {
        return ean != null && Long.parseLong(ean) == 0;
    }
}
