package com.app.update.scheduler.option.impl;

import java.util.List;

import com.app.update.scheduler.jamfpro.api.JssApi;

import javafx.concurrent.Task;
import javafx.scene.text.Text;

public class EvenlySpreadSchedulerOption extends Task<Boolean> {
	
	private static final String updateXml = "<mobile_device_application><general><itunes_sync_time>%d</itunes_sync_time></general></mobile_device_application>";
	
	private JssApi jssApi;
	private List<Integer> deviceIdList;
	private Text actiontarget;
	
	public EvenlySpreadSchedulerOption(JssApi jssApi, List<Integer> deviceIdList, Text actiontarget) {
		this.jssApi = jssApi;
		this.deviceIdList = deviceIdList;
		this.actiontarget = actiontarget;
	}

	@Override
	protected Boolean call() throws Exception {
		
		actiontarget.setText("Calculating spread of application updates");
		int count=0;
		double startTime = 0;
		double spread = 86400 / new Double(deviceIdList.size());
		
		try {
			actiontarget.setText("Updating application update schedules");
			
			for (int id : deviceIdList) {
                            
				jssApi.put("mobiledeviceapplications/id/" + id, String.format(updateXml, Math.round(startTime)));

				updateProgress(count, deviceIdList.size());
                                count++;
				startTime = startTime + spread;
			}
                        updateProgress(1, 1);
		} catch (Exception e) {
			actiontarget.setText("There was an error while processing app updates.");
			return false;
		}
                
		return true;
	}
}
