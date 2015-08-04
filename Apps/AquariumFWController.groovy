definition(
	name: "Aquarium FW Controller",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Controls Freshwater Aquarium Schedule",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet@2x.png"
);

preferences {
	section("Select switch...") { // AquairumFWStrip
		input(name: "strip", type: "capability.switch", multiple: false);
	}
}

def installed() {
	log.debug("Installed with settings: ${settings}");
	scheduleTimes();
}

def updated(settings) {
	log.debug('Application updated');
	unschedule();
	scheduleTimes();
}

def scheduleTimes() {
	log.debug('Scheduling times');

	// Display Tank
	// On
	schedule("0 0 6 ? * 2-6", startDisplay); // Mon - Fri
    schedule("0 0 10 ? * 1,7", startDisplay); // Sun, Sat
    // Mid-day Break
	schedule("0 0 10 ? * 2-6", stopDisplay); // Mon - Fri
	schedule("0 0 17 ? * 2-6", startDisplay); // Mon - Fri
    // Off
	schedule("0 0 21 ? * 1-5", stopDisplay); // Sun - Thu
    schedule("0 0 22 ? * 6-7", stopDisplay); // Fri - Sat

	// Refugium Tank
	schedule("0 0 17 * * ?", startRefugium); // Everyday
	schedule("0 0 8 ? * 2-6", stopRefugium); // Mon - Fri
    
    // Brackish Tank
	//schedule("0 0 6 * * ?", startBrackish); // brackish broke
	schedule("0 0 22 * * ?", stopBrackish);
    
    // Refugium Pump
	schedule("0 0 0 * * ?", stopUnused);
}

def startDisplay() {
	log.debug('Turning on display outlet');
	strip.on1();
}

def stopDisplay() {
	log.debug('Turning off display outlet');
	strip.off1();
}

def startRefugium() {
	log.debug('Turning on refugium outlet');
	strip.on2();
}

def stopRefugium() {
	log.debug('Turning off refugium outlet');
	strip.off2();
}

def startBrackish() {
	log.debug('Turning on brackish outlet');
	strip.on3();
}

def stopBrackish() {
	log.debug('Turning off brackish outlet');
	strip.off3();
}

def stopUnused() { // Used for Refugium Pump during later process
	log.debug('Turning off unused outlet');
	strip.off4();
}
