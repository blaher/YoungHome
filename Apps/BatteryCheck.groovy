definition(
	name: "Battery Check",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Checks devices with low batteries",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Allstate/power_allowance.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Allstate/power_allowance@2x.png"
);

preferences {
	section("Select devices...") {
		input "batteryDevices", "capability.battery", multiple: true
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}";
	scheduleTimes();
}

def updated(settings) {
	unsubscribe();
	scheduleTimes();
}

def scheduleTimes() {
	log.debug "Scheduling times";
	
	schedule("0 4 6 * * ?", checkBatteries);
}

def checkBatteries() {
	log.debug "Checking batteries";
	
	def whichDevice;
	def x=0;

	while (x < 255) {
		whichDevice = batteryDevices[x];
		if (whichDevice != null) {
			def battery = whichDevice.currentValue("battery");
			if (battery <= 10) {
				log.debug "The $whichDevice battery is low";
				sendPush("The $whichDevice battery is low.");
			}
		}
		x=x+1;
	} 
}
