package com.decathlon.log.rfid.pallet.autovalidation.task;

import com.decathlon.log.rfid.pallet.scan.reader.TagsListener;
import com.decathlon.log.rfid.pallet.service.TaskManagerService;
import com.decathlon.log.rfid.pallet.service.controller.SendHuWithContentTask;
import lombok.extern.log4j.Log4j;

import java.util.TimerTask;

@Log4j
public class AutoValidationSendHuTimerTask extends TimerTask {

    private final TaskManagerService taskManagerService;

    private final String hu;
    private final TagsListener tagsListener;
    private final TimerTask countdownTimerTask;

    public AutoValidationSendHuTimerTask(final String hu, final TagsListener tagsListener,
                                         final TimerTask countdownTimerTask) {
        this.hu = hu;
        this.tagsListener = tagsListener;
        this.countdownTimerTask = countdownTimerTask;
        this.taskManagerService = TaskManagerService.getInstance();
    }

    @Override
    public void run() {
        countdownTimerTask.cancel();
        log.debug("Auto validation send tags to server");
        taskManagerService.blockUIThenExecuteTask(new SendHuWithContentTask(hu, tagsListener.getScannedTags()));
    }
}
