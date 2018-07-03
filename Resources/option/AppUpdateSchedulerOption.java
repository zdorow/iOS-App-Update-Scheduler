package com.app.update.scheduler.option;

public enum AppUpdateSchedulerOption {

	EvenlySpread("Evenly spread throughout the day"), 
	TimeInterval("Use a specific time interval");
	
	private String displayText;
	
	AppUpdateSchedulerOption(String displayText) {
		this.displayText = displayText;
	}
	
	public String getDisplayText() {
		return displayText;
	}
	
	public static AppUpdateSchedulerOption fromDisplayText(String displayText) {
		for (AppUpdateSchedulerOption option : values()) {
			if (displayText.equals(option.getDisplayText())) {
				return option;
			}
		}
		return null;
	}
}
