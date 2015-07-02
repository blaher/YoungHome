definition(
	name: "Mode Home",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Does procedures required when coming home.",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/rise-and-shine.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/rise-and-shine@2x.png"
);

preferences {
	section('Select GPS') {
		input('gps', 'capability.switch', multiple: true);
	}
    
	section('Select Away Mode') {
		input('away_mode', 'mode');
	}
}

def installed() {
	log.debug("Installed with settings: ${settings}");
	
	subscribe(gps, 'switch.off', switchMode);
	subscribe(location, 'mode', modeAway)
}

def updated() {}

def switchMode(evt) {
	log.debug('Someone has arrived.');
	setLocationMode(away_mode);
}

def modeHome(evt) {
    log.debug("Mode changed to: ${evt.value}");
    
    if (evt.value == home_mode) {
    
    }
}
