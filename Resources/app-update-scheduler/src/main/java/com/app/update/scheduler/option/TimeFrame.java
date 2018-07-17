package com.app.update.scheduler.option;

import javafx.scene.text.Text;

public enum TimeFrame {

	Midnight("12 AM", 0), 
	OneAM("1 AM", 1), 
	TwoAM("2 AM", 2), 
	ThreeAM("3 AM", 3), 
	FourAM("4 AM", 4), 
	FiveAM("5 AM", 5), 
	SixAM("6 AM", 6), 
	SevenAM("7 AM", 7), 
	EightAM("8 AM", 8), 
	NineAM("9 AM", 9), 
	TenAM("10 AM", 10), 
	ElevenAM("11 AM", 11), 
	Noon("12 PM", 12),
	OnePM("1 PM", 13),
	TwoPM("2 PM", 14),
	ThreePM("3 PM", 15),
	FourPM("4 PM", 16),
	FivePM("5 PM", 17),
	SixPM("6 PM", 18),
	SevenPM("7 PM", 19),
	EightPM("8 PM", 20),
	NinePM("9 PM", 21),
	TenPM("10 PM", 22),
	ElevenPM("11 PM", 23);

	
	private final String displayText;
	private final int hoursFromMidnight;
	
	TimeFrame(String displayText, int hoursFromMidnight) {
		this.displayText = displayText;
		this.hoursFromMidnight = hoursFromMidnight;
	}
	
	public String getDisplayText() {
		return displayText;
	}
	
	public int calculateNumberOfSecondsFromMidnight() {
		return hoursFromMidnight * 60 * 60;
	}
	
	public static TimeFrame fromDisplayText(String displayText, Text actiontarget){
            try{
		for (TimeFrame option : values()) {
			if (displayText.equals(option.getDisplayText())) {
				return option;
			}
                        
		}
            } catch (NullPointerException e){ 
                actiontarget.setText("Please make a selection.");
            }
		return null;
	}
}
