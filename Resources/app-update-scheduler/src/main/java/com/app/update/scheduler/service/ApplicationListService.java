package com.app.update.scheduler.service;

import java.util.List;

import com.app.update.scheduler.applicationlistget.ApplicationListGet;
import com.app.update.scheduler.controller.form.AppUpdateForm;
import com.app.update.scheduler.eventhandler.JssApiResponseHandler;
import com.app.update.scheduler.jamfpro.api.JssApi;
import com.app.update.scheduler.option.AppUpdateSchedulerOption;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

public class ApplicationListService extends Service<List<Integer>> {
	
	private final ApplicationListGet applicationListGet;
	
	public ApplicationListService(JssApi jssApi, Text actiontarget, ProgressBar progressBar, Button button,
			ComboBox<String> timeFrameStartOptions, ComboBox<String> timeFrameEndOptions, AppUpdateSchedulerOption schedulerOption, AppUpdateForm appUpdateForm) {
		this.applicationListGet = new ApplicationListGet(jssApi, actiontarget, progressBar);
		
		progressBar.progressProperty().bind(applicationListGet.progressProperty());
		
		//applicationListGet.onSucceededProperty().bind(this.onSucceededProperty());
		applicationListGet.setOnSucceeded(e -> {
			System.out.println("ApplicationListService has succeeded.");

			List<Integer> appIdList = applicationListGet.getValue();
			System.out.println(appIdList);
			
			// Since ApplicationListService and TimeFrameSchedulerService are defined as two separate services, we should not be embedding
			// the latter inside the former. Let's try to find a way to either consolidate these or separate them out in the future.
			new TimeFrameSchedulerService(jssApi, appIdList, actiontarget, timeFrameStartOptions, timeFrameEndOptions, schedulerOption, progressBar, button, appUpdateForm).start();
		});
		
		applicationListGet.setOnFailed(new JssApiResponseHandler(jssApi, appUpdateForm));
	}

	@Override
	protected Task<List<Integer>> createTask() {
		return applicationListGet;
	}

	public ApplicationListGet getApplicationListGet() {
		return applicationListGet;
	}
}
