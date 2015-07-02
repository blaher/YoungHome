definition(
	name: "Mode Away",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Does procedures required when leaving.",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/bon-voyage.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/bon-voyage@2x.png"
);

preferences {
	section('Select GPS') {
		input('gps', 'capability.switch', multiple: true);
	}
    
	section('Select Mode') {
		input('away_mode', 'mode');
	}
}

def installed() {
	log.debug("Installed with settings: ${settings}");
	
	subscribe(gps, 'switch.off', switchMode);
	subscribe(location, 'mode', modeAway);
}

def updated() {}

def switchMode(evt) {
	log.debug('Someone has left.');
    
    def currSwitches = gps.currentSwitch;

    def onSwitches = currSwitches.findAll {
    	switchVal -> switchVal == "on" ? true : false;
    }

    if (onSwitches.size() == 0) {
	    setLocationMode(away_mode);
	}
}

def modeAway(evt) {
    log.debug("Mode changed to: ${evt.value}");
    
    if (evt.value == away_mode) {
    
    }
}
