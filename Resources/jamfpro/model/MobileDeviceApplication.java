package com.app.update.scheduler.jamfpro.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mobile_device_application")
@XmlAccessorType(XmlAccessType.FIELD)
public class MobileDeviceApplication {

	@XmlElement(name = "general")
	private MobileDeviceApplicationGeneral general;

	public MobileDeviceApplicationGeneral getGeneral() {
		return general;
	}

	public void setGeneral(MobileDeviceApplicationGeneral general) {
		this.general = general;
	}

	@Override
	public String toString() {
		return "MobileDeviceApplication [general=" + general + "]";
	}
}
