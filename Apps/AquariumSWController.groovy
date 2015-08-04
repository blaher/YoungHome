definition(
	name: "Aquarium SW Controller",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Controls Saltwater Aquarium Schedule",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet@2x.png"
);

preferences {
	section("Select switch...") { // AquairumSWStrip
		input(name: "strip", type: "capability.switch", multiple: false);
	}
}

def installed() {
	log.debug("Installed with settings: ${settings}");
	scheduleTimes();
    startUsed();
}

def updated(settings) {
	log.debug('Application updated');
	unschedule();
	scheduleTimes();
    startUsed();
}

def scheduleTimes() {
	log.debug('Scheduling times');

	// Front Light
    schedule("0 0 10 ? * 1,7", startFrontLight); // Sun, Sat
	schedule("0 0 6 ? * 2-6", startFrontLight); // Mon - Fri
    schedule("0 0 10 ? * 2-6", stopFrontLight); // Mon - Fri
    schedule("0 0 14 ? * 2-6", startFrontLight); // Mon - Fri
	schedule("0 0 18 ? * 1-5", stopFrontLight); // Sun - Thu
    schedule("0 0 20 ? * 6-7", stopFrontLight); // Fri, Sat

	// Back Light
    schedule("0 0 12 ? * 1,7", startBackLight); // Sun, Sat
	schedule("0 0 9 ? * 2-6", startBackLight); // Mon - Fri
    schedule("0 0 10 ? * 2-6", stopBackLight); // Mon - Fri
    schedule("0 0 14 ? * 2-6", startBackLight); // Mon - Fri
	schedule("0 0 21 ? * 1-5", stopBackLight); // Sun - Thu
    schedule("0 0 22 ? * 6-7", stopBackLight); // Fri, Sat
    
	// Unspecified Used Outlets
	schedule("0 0 0 * * ?", startUsed);
}

def startFrontLight() {
	log.debug('Turning on front light');
	strip.on1();
}

def stopFrontLight() {
	log.debug('Turning off front light');
	strip.off1();
    startUsed();
}

def startBackLight() {
	log.debug('Turning on back light');
	strip.on2();
}

def stopBackLight() {
	log.debug('Turning off back light');
	strip.off2();
    startUsed();
}

def startUsed() { // Used for other outlets for later use
	log.debug('Turning on used outlet');
	strip.on3();
	strip.on4();
}
