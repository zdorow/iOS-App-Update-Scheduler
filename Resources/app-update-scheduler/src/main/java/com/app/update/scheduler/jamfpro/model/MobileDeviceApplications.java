package com.app.update.scheduler.jamfpro.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mobile_device_applications")
@XmlAccessorType(XmlAccessType.FIELD)
public class MobileDeviceApplications {

	@XmlElement(name = "mobile_device_application")
	private List<MobileDeviceApplicationShell> mobileDeviceApplicationList;

	public List<MobileDeviceApplicationShell> getMobileDeviceApplicationList() {
		return mobileDeviceApplicationList;
	}

	public void setMobileDeviceApplicationList(List<MobileDeviceApplicationShell> mobileDeviceApplicationList) {
		this.mobileDeviceApplicationList = mobileDeviceApplicationList;
	}

	@Override
	public String toString() {
		return "MobileDeviceApplications [mobileDeviceApplicationList=" + mobileDeviceApplicationList + "]";
	}
	
	public static class MobileDeviceApplicationShell {
		
		private int id;
		private String name;
		
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "MobileDeviceApplicationShell [id=" + id + ", name=" + name + "]";
		}
	}
}
