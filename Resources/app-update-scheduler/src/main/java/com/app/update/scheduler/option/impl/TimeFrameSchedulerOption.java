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
			double spreadBeforeMidnight, spreadAfterMidnight;
			double startTime = timeFrameStart.calculateNumberOfSecondsFromMidnight();
                        double timeUntilMidnight = 86400 - startTime;
			double endTime = timeFrameEnd.calculateNumberOfSecondsFromMidnight();
			actiontarget.setText("Updating application update schedules");
			
			if(endTime < startTime)
			{
			System.out.println("Endtime less than start detected. Running compensation sequence");
			double midnight=0.0;
                        double calcualtePercentageList = timeUntilMidnight / 86400;
                        System.out.println("Percentage of list to be allocated before midnight" + calcualtePercentageList * 100);
			double percentListTotal=(Math.round(appIdList.size() * calcualtePercentageList));

			List<Integer> appIdListBeforeMidnight = appIdList.subList(0, (int)percentListTotal);
			List<Integer> appIdListAfterMidnight = appIdList.subList((int)percentListTotal, appIdList.size());
			
			spreadBeforeMidnight = timeUntilMidnight / new Double(appIdListBeforeMidnight.size());
			spreadAfterMidnight = endTime / new Double(appIdListAfterMidnight.size());
			
			for (int id : appIdListBeforeMidnight) {
				jssApi.put("mobiledeviceapplications/id/" + id, String.format(updateXml, Math.round(startTime)));
                                System.out.println("Spread Before midnight:" + spreadBeforeMidnight);
                                System.out.println("Start Time:" + startTime);
				updateProgress(count, appIdListBeforeMidnight.size());
				count++;
				startTime += spreadBeforeMidnight;
				Double percent = (double) count/appIdListBeforeMidnight.size()*100;
				actiontarget.setText("Updating applications that run before midnight. " + percent.intValue() + "%");
			}
			count=0;
			for (int id : appIdListAfterMidnight) {
				jssApi.put("mobiledeviceapplications/id/" + id, String.format(updateXml, Math.round(midnight)));

				updateProgress(count, appIdListAfterMidnight.size());
				count++;
				midnight += spreadAfterMidnight;
				Double percent = (double) count/appIdListAfterMidnight.size()*100;
				actiontarget.setText("Updating applications that run after midnight. " + percent.intValue() + "%");
			}
			}
			
			else {
			double spread = endTime / new Double(appIdList.size());
			
			for (int id : appIdList) {
				jssApi.put("mobiledeviceapplications/id/" + id, String.format(updateXml, Math.round(startTime)));

				updateProgress(count, appIdList.size());
				count++;
				startTime += spread;
				Double percent = (double) count/appIdList.size()*100;
				actiontarget.setText(percent.intValue() + "%");
			}
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