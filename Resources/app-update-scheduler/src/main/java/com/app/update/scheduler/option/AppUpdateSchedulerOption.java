package com.app.update.scheduler.option;

public enum AppUpdateSchedulerOption {

	EvenlySpread("Evenly spread throughout the day", false), 
	TimeInterval("Use a specific time interval", true);
	
	private final String displayText;
	private final boolean defaultSelectedValue;
	
	AppUpdateSchedulerOption(String displayText, boolean defaultSelectedValue) {
		this.displayText = displayText;
		this.defaultSelectedValue = defaultSelectedValue;
	}
	
	public String getDisplayText() {
		return displayText;
	}
	
	public boolean isDefaultSelectedValue() {
		return defaultSelectedValue;
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
