package com.decathlon.log.rfid.pallet.service;

import com.decathlon.log.rfid.pallet.ui.indicator.BusyIndicator;
import com.decathlon.log.rfid.pallet.ui.indicator.BusyIndicatorInputBlocker;
import com.decathlon.log.rfid.pallet.main.RFIDPalletApp;
import org.apache.log4j.Logger;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskService;

public class TaskManagerService {

    private final static Logger LOGGER = Logger.getLogger(TaskManagerService.class);

    private static TaskManagerService instance;

    private TaskService taskService;

    private TaskManagerService() {
        taskService = RFIDPalletApp.getApplication().getContext().getTaskService(RFIDPalletApp.TASK_SERVICE_NAME);
    }

    public static TaskManagerService getInstance() {
        synchronized (TaskManagerService.class) {
            if (instance == null) {
                instance = new TaskManagerService();
            }
            return instance;
        }
    }

    public void execute(final Task<Object, Void> task) {
        taskService.execute(task);
    }

    public Task blockUIThenExecuteTask(final Task<Object, Void> task) {
        blockTask(task);
        taskService.execute(task);
        return task;
    }

    public Task blockTask(final Task<Object, Void> task) {
        BusyIndicator glassPaneComponentBusyIndicator = new BusyIndicator();
        RFIDPalletApp.getApplication().getMainFrame().getRootPane().setGlassPane(glassPaneComponentBusyIndicator);
        task.setInputBlocker(new BusyIndicatorInputBlocker(task, glassPaneComponentBusyIndicator));
        return task;
    }

}