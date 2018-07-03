package com.app.update.scheduler.jamfpro.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MobileDeviceApplicationGeneral {

	private int id;
	
	@XmlElement(name = "itunes_sync_time")
	private int iTunesSyncTime;
	
	@XmlElement(name = "keep_description_and_icon_up_to_date")
	private boolean keepDescriptionAndIconUpToDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getiTunesSyncTime() {
		return iTunesSyncTime;
	}

	public void setiTunesSyncTime(int iTunesSyncTime) {
		this.iTunesSyncTime = iTunesSyncTime;
	}

	public boolean isKeepDescriptionAndIconUpToDate() {
		return keepDescriptionAndIconUpToDate;
	}

	public void setKeepDescriptionAndIconUpToDate(boolean keepDescriptionAndIconUpToDate) {
		this.keepDescriptionAndIconUpToDate = keepDescriptionAndIconUpToDate;
	}

	@Override
	public String toString() {
		return "MobileDeviceApplicationGeneral [id=" + id + ", iTunesSyncTime=" + iTunesSyncTime
				+ ", keepDescriptionAndIconUpToDate=" + keepDescriptionAndIconUpToDate + "]";
	}
}
