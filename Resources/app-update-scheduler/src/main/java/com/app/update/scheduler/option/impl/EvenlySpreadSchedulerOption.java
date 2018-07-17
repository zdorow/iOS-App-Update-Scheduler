package com.app.update.scheduler.option.impl;

import com.app.update.scheduler.jamfpro.api.JssApi;
import com.app.update.scheduler.jamfpro.api.JssApiException;
import java.util.List;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.text.Text;

public class EvenlySpreadSchedulerOption extends Task<Boolean> {
	
	private static final String updateXml = "<mobile_device_application><general><itunes_sync_time>%d</itunes_sync_time></general></mobile_device_application>";
        private static final Logger LOG = Logger.getLogger(EvenlySpreadSchedulerOption.class.getName());
	
	private final JssApi jssApi;
	private final List<Integer> appIdList;
	private final Text actiontarget;
	
	public EvenlySpreadSchedulerOption(JssApi jssApi, List<Integer> deviceIdList, Text actiontarget) {
		this.jssApi = jssApi;
		this.appIdList = deviceIdList;
		this.actiontarget = actiontarget;
	}

	@Override
	protected Boolean call() throws Exception {

		actiontarget.setText("Calculating spread of application updates");
		int count=0;
		double startTime = 0;
		double spread = 86400 / new Double(appIdList.size());
		
		try {
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
		} catch (JssApiException e) {
			actiontarget.setText("There was an error while processing app updates.");
			return false;
		}
                
		return true;
	}
}