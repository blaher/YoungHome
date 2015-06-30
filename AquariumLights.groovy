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
	section("Select switch...") {
		input name: "switch", type: "capability.switch", multiple: false
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}";
    setupTimes();
}

def updated(settings) {
	unschedule();
    setupTimes();
}

def setupTimes() {
	log.debug "Scheduling times";
    // Times are EST subtract 6

	schedule("0 0 0 * * ?", startDisplay);
	schedule("0 0 5 * 2-6 ?", stopDisplay);
	schedule("0 0 8 * 2-6 ?", startDisplay);
	schedule("0 0 16 * * ?", stopDisplay);

	schedule("0 0 11 * * ?", startRefugium);
	schedule("0 0 2 * 2-6 ?", stopRefugium);
    
	schedule("0 0 0 * * ?", startBrackish);
	schedule("0 0 16 * * ?", stopBrackish);
    
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

def stopUnused() {
	log.debug "Turning off unused outlet";
	strip.off4();
}
