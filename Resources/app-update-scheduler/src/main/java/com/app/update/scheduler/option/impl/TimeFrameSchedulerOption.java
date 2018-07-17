package com.app.update.scheduler.option.impl;

import java.util.List;

import com.app.update.scheduler.jamfpro.api.JssApi;
import com.app.update.scheduler.jamfpro.api.JssApiException;
import com.app.update.scheduler.option.TimeFrame;

import javafx.concurrent.Task;
import javafx.scene.text.Text;

public class TimeFrameSchedulerOption extends Task<Boolean> {

	private static final String updateXml = "<mobile_device_application><general><itunes_sync_time>%d</itunes_sync_time></general></mobile_device_application>";

	private final JssApi jssApi;
	private final List<Integer> appIdList;
	private final Text actiontarget;
	private final TimeFrame timeFrameStart;
	private final TimeFrame timeFrameEnd;

	public TimeFrameSchedulerOption(JssApi jssApi, List<Integer> appIdList, Text actiontarget, TimeFrame timeFrameStart, TimeFrame timeFrameEnd) {
		this.jssApi = jssApi;
		this.appIdList = appIdList;
		this.actiontarget = actiontarget;
		this.timeFrameStart = timeFrameStart;
		this.timeFrameEnd = timeFrameEnd;
	}

	@Override
	protected Boolean call() throws Exception {

		actiontarget.setText("Calculating spread of application updates");

		try {
			int count=0;
			double startTime = timeFrameStart.calculateNumberOfSecondsFromMidnight();
			double endTime = timeFrameEnd.calculateNumberOfSecondsFromMidnight();

			double spread = endTime / new Double(appIdList.size());

			actiontarget.setText("Updating application update schedules");

			for (int id : appIdList) {
				jssApi.put("mobiledeviceapplications/id/" + id, String.format(updateXml, Math.round(startTime)));

				updateProgress(count, appIdList.size());
				count++;
				startTime += spread;
				Double percent = (double) count/appIdList.size()*100;
				actiontarget.setText( percent.intValue() + "%");
			}
			updateProgress(1, 1);
			appIdList.clear();
		} catch (JssApiException e) {
			actiontarget.setText("There was an error while processing app updates.");
			return false;
		}

		return true;
	}
}