definition(
	name: "Aquarium Water Change Controller",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Controls Aquarium Water Change Schedule",
	category: "Pets",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet@2x.png"
);

preferences {
  section("Select strip...") { // AquairumStrip
		input name: "strip", type: "capability.switch", multiple: false
	}
	section("Select switch...") { // AquairumSwitch
		input name: "switch", type: "capability.switch", multiple: false
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}";
	setupTimes();
}

def updated(settings) {
	unschedule();
	scheduleTimes();
}

def scheduleTimes() {
	log.debug "Scheduling times";
	// Times are EST subtract 6

  schedule("0 0 4 * 2-6 ?", startMix);
  schedule("0 0 6 * 2-6 ?", stopRefugium);
	schedule("0 1 6 * 2-6 ?", startDrain);
	schedule("0 0 7 * 2-6 ?", stopDrain);
	schedule("0 0 7 * 2-6 ?", stopMix);
	schedule("0 1 7 * 2-6 ?", startPump);
	schedule("0 0 8 * 2-6 ?", stopPump);
	schedule("0 1 8 * 2-6 ?", startRefugium);
}

def startMix() {
  log.debug "Starting Mix...";
}
def stopMix() {
  log.debug "Stopping Mix...";
}

def startDrain() {
  log.debug "Starting Drain...";
}
def stopDrain() {
  log.debug "Stopping Drain...";
}

def startPump() {
  log.debug "Starting Pump...";
  switch.on();
}
def stopPump() {
  log.debug "Stopping Pump...";
  switch.off();
}

def startRefugium() {
  log.debug "Starting Refugium...";
  strip.on4();
}
def stopRefugium() {
  log.debug "Stopping Refugium...";
  strip.off4();
}
