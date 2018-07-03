## ![logo](Resources/update.png)iOS Application Update Scheduler for Jamf|PRO

Welcome to the iOS Application Update Scheduler Page! This is a Java application that uses the Jamf PRO Classic API to divide up iOS application updates over a selected period of time. This will help spread the load on a Jamf PRO server that is put on by applications all trying to update at once. 

Requirements:

 1. Java SE Development Kit 8: [MacOS Java Download Page](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 2. Global App updates turned off in Jamf Pro and turned on for all the apps we want to have auto-updates turned on for.
 
 This is the global setting we need disabled to turn on and schedule updates at the individual app level. This setting is not accessible via the API:
 
![alt text](Resources/Yes.png)
 
 **To be added soon are scripts to help setup and configure turning on and off auto-app updates for iOS apps on the individual level. 
 
To run the program please download the .pkg and run the installer. It will get installed to the Applications folder and can be run from there.

[MacOS Download](App-Update-Scheduler.pkg)

Happy Scheduling! 

Source code can be found here [Bryan's Page](https://github.com/blarson007/app-update-scheduler) and here [Zach's Page](https://github.com/zdorow/app-update-scheduler)

[MIT license](https://github.com/zdorow/iOS-App-Update-Scheduler/blob/master/Resources/LICENSE)
