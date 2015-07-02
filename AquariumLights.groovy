definition(
	name: "Aquarium Lights Controller",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Controls Aquarium Light Schedule",
	category: "Pets",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet@2x.png"
);

preferences {
	section("Select switch...") { // AquairumStrip
		input name: "strip", type: "capability.switch", multiple: false
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}";
	scheduleTimes();
}

def updated(settings) {
	unschedule();
	scheduleTimes();
}

def scheduleTimes() {
	log.debug "Scheduling times";

	schedule("0 0 6 * * ?", startDisplay);
	schedule("0 0 11 * 2-6 ?", stopDisplay);
	schedule("0 0 14 * 2-6 ?", startDisplay);
	schedule("0 0 22 * * ?", stopDisplay);

	schedule("0 0 17 * * ?", startRefugium);
	schedule("0 0 8 * 2-6 ?", stopRefugium);
    
	schedule("0 59 5 * * ?", startBrackish);
	schedule("0 1 22 * * ?", stopBrackish);
    
	schedule("0 0 0 * * ?", stopUnused);
}

def startDisplay() {
	log.debug "Turning on display outlet";
	strip.on1();
}

def stopDisplay() {
	log.debug "Turning off display outlet";
	strip.off1();
}

def startRefugium() {
	log.debug "Turning on refugium outlet";
	strip.on2();
}

def stopRefugium() {
	log.debug "Turning off refugium outlet";
	strip.off2();
}

def startBrackish() {
	log.debug "Turning on brackish outlet";
	strip.on3();
}

def stopBrackish() {
	log.debug "Turning off brackish outlet";
	strip.off3();
}

def stopUnused() { // Used for Refugium Pump during later process
	log.debug "Turning off unused outlet";
	strip.off4();
}
