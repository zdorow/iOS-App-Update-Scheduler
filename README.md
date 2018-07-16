## ![logo](Resources/update.png)iOS Application Update Scheduler Version 1.0 for Jamf|PRO

Welcome to the iOS Application Update Scheduler Page! Where we are trying to save auto-app updates!

This is a Java application that uses the Jamf PRO Classic API to divide up iOS application updates over a selected period of time. This will help spread the load on a Jamf PRO server that is put on by applications all trying to update at once. It will only schedule the apps that have updates turned on at the individual level. 

***Who is this for?***

This is for small to medium sized customers. If we run this program and we are still seeing issues we might end up turning auto-app updates off until the larger issues are resolved. Admins who use iPad carts will likely be happier with an [Apple Configurator 2 workflow.](https://help.apple.com/configurator/mac/2.7.1/#/cad789a3f0bd)

Requirements:

 1. Java SE Development Kit 8: [MacOS Java Download Page](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 
 2. Global iOS App updates turned off in Jamf Pro and auto-updates turned on for the apps we would like to use this function. If we want to turn on app updates for **ALL** apps at the individual app level feel free to run the [Setup Script](Resources/AppUpdateSetup.sh)
 
**Note: The is the global setting we need disabled to turn on and schedule updates at the individual app level is found at Settings > Device Management > App Maintenance.**
 
This setting is not accessible via the API, however there are plans to integrate this function in a future release. 
 
If this it the first time setup we can use the Bash [Setup Script.](Resources/AppUpdateSetup.sh) This setup script has the option to download the .pkg when it is run and checks all the apps to make sure **ALL** iOS app updates are turned on at the individual level.
 
To run the program please download the .pkg and run the installer or run the setup script. It will get installed to the Applications folder and can be run from there.

[MacOS Download](App-Update-Scheduler.pkg)

Happy Scheduling! 

If we would like to turn off app updates for all iOS apps this script here will do the trick: [Turn OFF Updates Script](Resources/AppUpdateOff.sh)

_______________________________________________________________________________________________________________________

FUTURE PLANS:

 - Add support for MacOS apps
 - Integrate the functions of the scripts into the app.
 - Fancy interface updates coming soon!

Any ideas or suggestions are welcomed! 

If we run this program and are still seeing issues with iOS auto-app updates please contact [Jamf Support.](https://www.jamf.com/support)

Source code can be found on [Bryan's Page](https://github.com/blarson007/app-update-scheduler) and [Zach's Page](https://github.com/zdorow/app-update-scheduler) and also in the Resources folder of this Repo.

[MIT license](https://github.com/zdorow/iOS-App-Update-Scheduler/blob/master/Resources/LICENSE)
