package com.app.update.scheduler.controller.validator;

import com.app.update.scheduler.controller.form.AppUpdateForm;
import com.app.update.scheduler.option.AppUpdateSchedulerOption;
import org.apache.commons.lang3.StringUtils;

public class AppUpdateValidator {

	private final AppUpdateForm appUpdateForm;
	private int errorCount;
	
	public AppUpdateValidator(AppUpdateForm appUpdateForm) {
		this.appUpdateForm = appUpdateForm;
	}
	
	public void validate() {
		if (StringUtils.isEmpty(appUpdateForm.getJamfProServerUrl().getText())) {
			markError("Please input the URL of the Jamf PRO server");
			return;
		}
		
		if (StringUtils.isEmpty(appUpdateForm.getUserName().getText())) {
			markError("Please enter your Jamf PRO username");
			return;
		}
		
		if (StringUtils.isEmpty(appUpdateForm.getPassword().getText())) {
			markError("Please enter your password");
			return;
		}
		
		AppUpdateSchedulerOption schedulerOption = AppUpdateSchedulerOption.fromDisplayText(appUpdateForm.getAppSchedulerOptions().getValue());
		if (schedulerOption == AppUpdateSchedulerOption.TimeInterval) {
			if (StringUtils.isEmpty(appUpdateForm.getTimeFrameStartOptions().getValue())) {
				markError("Please select a Start Time");
				return;
			}
			
			if (StringUtils.isEmpty(appUpdateForm.getTimeFrameEndOptions().getValue())) {
				markError("Please select an End Time");
				return;
			}
		}
	}
	
	public boolean hasErrors() {
		return errorCount > 0;
	}
	
	private void markError(String errorMessage) {
		appUpdateForm.getActiontargetPane().getStyleClass().clear();
		appUpdateForm.getActiontargetPane().getStyleClass().add("alert");
		appUpdateForm.getActiontargetPane().getStyleClass().add("alert-danger");
		appUpdateForm.getActiontarget().setText(errorMessage);
		
		errorCount++;
	}
}
