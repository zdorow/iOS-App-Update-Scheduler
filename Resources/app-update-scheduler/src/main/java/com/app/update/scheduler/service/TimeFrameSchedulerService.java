package com.app.update.scheduler.service;

import com.app.update.scheduler.controller.form.AppUpdateForm;
import com.app.update.scheduler.eventhandler.JssApiResponseHandler;
import com.app.update.scheduler.jamfpro.api.JssApi;
import com.app.update.scheduler.option.AppUpdateSchedulerOption;
import com.app.update.scheduler.option.TimeFrame;
import com.app.update.scheduler.option.impl.EvenlySpreadSchedulerOption;
import com.app.update.scheduler.option.impl.TimeFrameSchedulerOption;
import java.util.List;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

public class TimeFrameSchedulerService extends Service<Boolean> {
    private static final Logger LOG = Logger.getLogger(TimeFrameSchedulerService.class.getName());
	
	private Task<Boolean> scheduler;
	
	public TimeFrameSchedulerService(JssApi jssApi, List<Integer> appIdList, Text actiontarget, ComboBox<String> timeFrameStartOptions, ComboBox<String> timeFrameEndOptions, 
			AppUpdateSchedulerOption schedulerOption, ProgressBar progressBar, Button button, AppUpdateForm appUpdateForm) {
		
		switch (schedulerOption) {
		case EvenlySpread:
			scheduler = new EvenlySpreadSchedulerOption(jssApi, appIdList, actiontarget);
			break;
		case TimeInterval:
			TimeFrame timeFrameStart = TimeFrame.fromDisplayText(timeFrameStartOptions.getValue(), actiontarget);
            TimeFrame timeFrameEnd = TimeFrame.fromDisplayText(timeFrameEndOptions.getValue(), actiontarget);
            scheduler = new TimeFrameSchedulerOption(jssApi, appIdList, actiontarget, timeFrameStart, timeFrameEnd);
			break;
		default:
            actiontarget.setText("Please make a selection.");
			break;
		}
		
		progressBar.progressProperty().bind(scheduler.progressProperty());
		
		scheduler.setOnSucceeded(ex -> {
			System.out.println("App Scheduling has completed.");
			System.out.println("TimeFrameSchedulerService has succeeded.");
			appIdList.clear();
			
			button.setDisable(false);
			actiontarget.setText("Done scheduling apps.");
		});
		scheduler.setOnFailed(new JssApiResponseHandler(jssApi, actiontarget, button, appUpdateForm));
	}

	@Override
	protected Task<Boolean> createTask() {
		return scheduler;
	}
}
