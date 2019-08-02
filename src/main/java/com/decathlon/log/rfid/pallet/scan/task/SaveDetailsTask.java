package com.decathlon.log.rfid.pallet.scan.task;

import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import com.decathlon.log.rfid.pallet.repository.BoExpedition;
import com.decathlon.log.rfid.pallet.scan.reader.TagsListener;
import com.decathlon.log.rfid.pallet.utils.RFIDProperties;
import org.apache.log4j.Logger;
import org.jdesktop.application.Task;

import java.util.List;

/**
 * Save sgtin information by WS call
 *
 * @author agodin16
 */
public class SaveDetailsTask extends Task<Object, Void> {
    private Logger log = Logger.getLogger(SaveDetailsTask.class);

    private BoExpedition boExpedition;
    private String uat;
    private List<String> tags;

    /**
     * Constructor.
     *
     * @param tagsHandlerDeprecated
     * @param uat
     */
    public SaveDetailsTask(List<String> tags, String uat) {
        super(RFIDPalletApp.getApplication());
        this.boExpedition = new BoExpedition();
        this.uat = uat;
        this.tags = tags;
    }

    /**
     * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
     */
    @Override
    protected Object doInBackground() throws Exception {
        final String mat = RFIDProperties.getValue(RFIDProperties.PROPERTIES.MAT);
        log.info(mat + " Sending " + tags.size() + " tags to WS for pallet " + uat);
        return boExpedition.saveDetailsForUat(tags, uat, mat);
    }

    /**
     * @see org.jdesktop.application.Task#succeeded(java.lang.Object)
     */
    @Override
    protected void succeeded(Object result) {
        super.succeeded(result);
    }

    /**
     * @see org.jdesktop.application.Task#failed(java.lang.Throwable)
     */
    @Override
    protected void failed(Throwable cause) {
        super.failed(cause);
    }

    /**
     * @see org.jdesktop.application.Task#finished()
     */
    @Override
    protected void finished() {
        super.finished();
    }


}
