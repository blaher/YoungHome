definition(
	name: "Mode Sleep",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Does procedures required when going to bed.",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/good-night.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/good-night@2x.png"
);

preferences {
	section('Select Mode') {
		input('night_mode', 'mode');
	}
}

def installed() {
	log.debug("Installed with settings: ${settings}");
	
	subscribe(location, 'mode', modeNight);
}

def updated() {}

def modeNight(evt) {
    log.debug("Mode changed to: ${evt.value}");
    
    if (evt.value == night_mode) {
    
    }
}
