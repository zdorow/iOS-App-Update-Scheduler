#!/bin/bash
# Script to change apps settings for auto update.


status=99
until [ $status -eq 0 ]; do
    # Username Entry
    echo ""
    echo "Please enter the Jamf Pro API username: "
    read apiUser
    echo ""
    # Password Entry
    echo "Please enter the password: "
    read -s apiPass
    echo ""

    # URL of Jamf Pro server entry
    echo "Please enter the Jamf Pro URL including the port if we are locally hosted"
    echo "ex. https://jamfit.jamfsw.com:8443"
    echo ""
    echo "No port needed for cloud hosted instances ex. https://jamfit.jamfsw.com"
    read url
    echo ""

    # Removal of trailing slash if found in url
    if [ $(echo "${url: -1}") = "/" ]; then
    	url=$(echo $url | sed 's/.$//')
    fi

    test=$(/usr/bin/curl --fail -ksu "$apiUser":"$apiPass" "$url/JSSResource/mobiledeviceapplications" -X GET)
    status=$?
    if [ $status -eq 6 ]; then
    	echo ""
    	echo "The Jamf Pro URL is reporting an error. Please try again."
    	echo "If the error persists please check permissions and internet connection."
    	echo ""
    #	exit 99
    elif [ $status -eq 22 ]; then
    	echo ""
    	echo "Username and/or password is incorrect."
    	echo "If the error persists please check permissions and internet connection."
    	echo ""
    #	exit 99
    elif [ $status -eq 0 ]; then
        echo ""
        echo "Connection test successful!"
    else
        echo ""
        echo "Something really went wrong,"
        echo "Lets try this again."
    fi
done

function jssversion () {
        version=$(curl -ks "$url" | grep "<title>" | cut -d v -f2 | cut -d\< -f1 | cut -d '-' -f1)
        #If version was empty, we ran into SSO, go to Failover
        if [[ $version == "" ]]; then
            version=$(curl -ks "$url/?failover" | grep "<title>" | cut -d v -f2 | cut -d\< -f1 | cut -d '-' -f1)
        fi
}

jssversion
echo "Jamf Pro version is $version"
required_version="10.7.0"
if [ "$(printf '%s\n' "$required_version" "$version" | sort -V | head -n1)" = "$required_version" ]; then
    echo "Jamf Pro Version 10.7.0+ verfied"
else
    echo "Jamf Pro version 10.7.0 or above required. Exiting script."
    exit 99
fi 

appOption=0
until [ $appOption -eq 1 ] || [ $appOption -eq 2 ] || [ $appOption -eq 3 ]; do
    echo ""
    echo "Enter 1 to turn off \"keep description and icon up to date\" "
    echo "Enter 2 to turn off \"keep_app_updated_on_device\""
    echo "Enter 3 to turn off both options. "
    read appOption
    echo ""
    if [ $appOption -eq 1 ] || [ $appOption -eq 2 ] || [ $appOption -eq 3 ]; then
    echo ""
    echo "Running option $appOption. Press CNTRL+C to stop the process."
    echo ""
    else
    echo ""
    echo "Invalid option. Please pick 1, 2 or 3."
    echo ""
    fi
done
echo ""
# Clear out variables
index="0"
IDs=()
#Get a list of apps ID
IDs=`curl -sk -H "Accept: application/xml" -u $apiUser:$apiPass ${url}/JSSResource/mobiledeviceapplications -X GET`
size=`echo $IDs | xpath //mobile_device_applications/size 2>/dev/null | sed 's/<[^>]*>//g'`
#echo "IDs: $IDs"
echo ""
echo $size "Mobile Device Apps objects will be scanned."
echo ""
#Put the IDs into an array (variable that will list every ID)
while [[ $index -lt ${size} ]]; do
	index=$[$index+1]
	apps+=(`echo $IDs | xpath //mobile_device_applications/mobile_device_application[${index}]/id 2>/dev/null | sed 's/<[^>]*>//g'`)
done

# Sort through each app ID individually
for i in "${apps[@]}"; do
	echo "Checking on Mobile Device App ID: ${i}"
	appID=`curl -sk -H "Accept: application/xml" -u $apiUser:$apiPass ${url}/JSSResource/mobiledeviceapplications/id/${i} -X GET`

    #Search the xml file on line above for specific tag, assign to variable
    updateIcon=`echo $appID | xpath //mobile_device_application/general/keep_description_and_icon_up_to_date 2>/dev/null | sed 's/<[^>]*>//g'`
    updateApp=`echo $appID | xpath //mobile_device_application/general/keep_app_updated_on_devices 2>/dev/null | sed 's/<[^>]*>//g'`
    echo ""
    echo "Update Icon staus: $updateIcon"
    echo ""
    echo "Update App status: $updateApp"
    echo ""

    case "$appOption" in
     # If update is true then make changes based on the option chosen.
        1)if [[ "$updateIcon" == "false" ]]; then
            echo ""
            echo "Icon and description updates have already been turned off for App id ${i}"
            echo ""
        elif [[ "$updateIcon" == "true" ]]; then
            echo ""
            echo "Turning off icon updates for App ID ${i}"
            echo ""
            curl -ku $apiUser:$apiPass -H "Content-Type: text/xml" $url/JSSResource/mobiledeviceapplications/id/${i} -d "<?xml version=\"1.0\" encoding=\"UTF-8\"?><mobile_device_application><general><keep_description_and_icon_up_to_date>false</keep_description_and_icon_up_to_date></general></mobile_device_application>" -X PUT
        else
            echo ""
            echo "***Warning***"
            echo "Unable to check the setting via the API for App ID ${i}"
            echo "This app settings will not be applied unless deleted and re-added."
            echo ""
        fi
        ;;
        2)if [[ "$updateApp" == "false" ]]; then
            echo ""
            echo "App updates on deivces have already been turned off for App id ${i}"
            echo ""
        elif [[ "$updateApp" == "true" ]]; then 
            echo ""
            echo "Turning off keeping the app updated on deivce for App ID ${i}"
            echo ""
            curl -ku $apiUser:$apiPass -H "Content-Type: text/xml" $url/JSSResource/mobiledeviceapplications/id/${i} -d "<?xml version=\"1.0\" encoding=\"UTF-8\"?><mobile_device_application><general><keep_app_updated_on_devices>false</keep_app_updated_on_devices></general></mobile_device_application>" -X PUT
        else
            echo ""
            echo "***Warning***"
            echo "Unable to check the setting via the API for App ID ${i}"
            echo "This app settings will not be applied unless deleted and re-added."
            echo ""
        fi
        ;;
        3)if [[ "$updateIcon" == "false" ]]  && [[ "$updateApp" == "false" ]]; then
            echo ""
            echo "App updates on deivces and icons have already been turned off for App id ${i}"
            echo ""
        elif [[ "$updateIcon" == "true" ]]  && [[ "$updateApp" == "false" ]]; then
            echo ""
            echo "Turning off icon updates for App ID ${i}"
            echo ""
            curl -ku $apiUser:$apiPass -H "Content-Type: text/xml" $url/JSSResource/mobiledeviceapplications/id/${i} -d "<?xml version=\"1.0\" encoding=\"UTF-8\"?><mobile_device_application><general><keep_description_and_icon_up_to_date>false</keep_description_and_icon_up_to_date></general></mobile_device_application>" -X PUT
        elif [[ "$updateIcon" == "false" ]]  && [[ "$updateApp" == "true" ]]; then
            echo ""
            echo "Turning off keeping the app updated on deivce for App ID ${i}"
            echo ""
            curl -ku $apiUser:$apiPass -H "Content-Type: text/xml" $url/JSSResource/mobiledeviceapplications/id/${i} -d "<?xml version=\"1.0\" encoding=\"UTF-8\"?><mobile_device_application><general><keep_app_updated_on_devices>false</keep_app_updated_on_devices></general></mobile_device_application>" -X PUT
        elif [[ "$updateIcon" == "true" ]]  && [[ "$updateApp" == "true" ]]; then
            echo ""
            echo "Turning off icon and device updates for App ID ${i}"
            echo ""
            curl -ku $apiUser:$apiPass -H "Content-Type: text/xml" $url/JSSResource/mobiledeviceapplications/id/${i} -d "<?xml version=\"1.0\" encoding=\"UTF-8\"?><mobile_device_application><general><keep_description_and_icon_up_to_date>false</keep_description_and_icon_up_to_date><keep_app_updated_on_devices>false</keep_app_updated_on_devices></general></mobile_device_application>" -X PUT
        else
            echo ""
            echo "***Warning***"
            echo "Unable to check the setting via the API for App ID ${i}"
            echo "This app settings will not be applied unless deleted and re-added."
            echo ""
        fi
        ;;
        *)echo ""
          echo "***Something really went wrong! Exiting script!***"
          echo ""
          exit 99
        ;;
    esac
done

echo ""
echo "The script has completed its run through the apps."
echo ""
exit 0
