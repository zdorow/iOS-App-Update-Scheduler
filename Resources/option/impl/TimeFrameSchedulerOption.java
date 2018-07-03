package com.app.update.scheduler.option.impl;

import java.util.List;

import com.app.update.scheduler.jamfpro.api.JssApi;
import com.app.update.scheduler.option.TimeFrame;

import javafx.concurrent.Task;
import javafx.scene.text.Text;

public class TimeFrameSchedulerOption extends Task<Boolean> {
	
	private static final String updateXml = "<mobile_device_application><general><itunes_sync_time>%d</itunes_sync_time></general></mobile_device_application>";

	private JssApi jssApi;
	private List<Integer> deviceIdList;
	private Text actiontarget;
	private TimeFrame timeFrameStart;
	private TimeFrame timeFrameEnd;
	
	public TimeFrameSchedulerOption(JssApi jssApi, List<Integer> deviceIdList, Text actiontarget, TimeFrame timeFrameStart, TimeFrame timeFrameEnd) {
		this.jssApi = jssApi;
		this.deviceIdList = deviceIdList;
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
			
			double spread = endTime / new Double(deviceIdList.size());
		
			actiontarget.setText("Updating application update schedules");
			
			for (int id : deviceIdList) {
				jssApi.put("mobiledeviceapplications/id/" + id, String.format(updateXml, Math.round(startTime)));
				
				updateProgress(count, deviceIdList.size());
                                count++;
				startTime = startTime + spread;
			}
                        updateProgress(1, 1);
		} catch (Exception e) {
			e.printStackTrace();
			actiontarget.setText("There was an error while processing app updates.");
			return false;
		}
		
		return true;
	}
}
