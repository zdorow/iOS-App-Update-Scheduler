#!/bin/bash
# Script to check apps settings for auto update.
status=1
until [ $status -eq 0 ]; do
    #Username Entry
    echo ""
    echo "Please enter the Jamf Pro API username: "
    read apiUser

    echo ""
    #Password Entry
    echo "Please enter the password: "
    read -s apiPass
    echo ""

    #URL of Jamf Pro server entry
    echo "Please enter the Jamf Pro URL including the port if we are locally hosted"
    echo "ex. https://jamfit.jamfsw.com:8443"
    echo ""
    echo "No port needed for cloud hosted instances ex. https://jamfit.jamfsw.com"
    read url
    echo ""

    #Removal of trailing slash if found in url
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
    elif [ $status -eq 22 ]; then
    	echo ""
    	echo "Username and/or password is incorrect."
    	echo "If the error persists please check permissions and internet connection."
    	echo ""
    elif [ $status -eq 0 ]; then
        echo ""
        echo "Connection test successful!"
    else
        echo ""
        echo "Something really went wrong,"
        echo "Lets try this again."
    fi
done

# This is the conditional for all apps
echo "" # secret flag fails to newline
#clear out variables
index="0"
IDs=()
#Get a list of apps ID
IDs=`curl -sk "Accept: application/xml" -H "Content-Type: application/xml" -u $apiUser:$apiPass ${url}/JSSResource/mobiledeviceapplications -X GET`
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
	appID=`curl -sk "Accept: application/xml" -H "Content-Type: application/xml" -u $apiUser:$apiPass ${url}/JSSResource/mobiledeviceapplications/id/${i} -X GET`
    #Search the xml file on line above for specific tag, assign to variable
    update=`echo $appID | xpath //mobile_device_application/general/keep_description_and_icon_up_to_date 2>/dev/null | sed 's/<[^>]*>//g'`
    if [[ "$update" == "false" ]]; then
        echo "App Updates: Disabled"
        echo ""
    elif [[ "$update" == "true" ]]; then
        echo "App Updates: Enabled"
        updateTime=`echo $appID | xpath //mobile_device_application/general/itunes_sync_time 2>/dev/null | sed 's/<[^>]*>//g'`
        let updateTimeMinutes=$updateTime/60
        updateTimeHours=`python -c "print $updateTimeMinutes/60.0"`
        echo "The iTunes Sync Time is $updateTime Seconds after Midnight"
        echo "The iTunes Sync Time is $updateTimeMinutes Minutes after Midnight"
        echo "The iTunes Sync Time is $updateTimeHours Hours after Midnight"
        echo ""
    fi
done

exit 0
