definition(
	name: "Holiday Christmas",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Flashes the entire house's colors, and plays random christmas music over sonos while away and movement is detected in front.",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Developers/smart-light-timer.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-iconsDevelopers/smart-light-timer@2x.png"
);


preferences {
	section('Select Lights') {
		input('lightsOdd', 'capability.colorControl', title:'Odd', multiple:true);
		input('lightsEven', 'capability.colorControl', title:'Even', multiple:true);
	}
}

def installed() {
	log.debug("Installed with settings: ${settings}");
}

def updated() {
	log.debug('App updated');
    
    states();
    schedules();
}

def states() {
	log.debug('Defining intial states');
    
    state.isOn = false;
}

def schedules() {
	log.debug('Scheduling events');

    schedule('0 0 5 1-25 12 ?', lightsOn);
    schedule('0 0 8 1-25 12 ?', lightsOff);
}

def lightsOn() {
	log.debug('Lights on');

	state.isOn = true;
    
    lightsOdd.setColorTemperature(0);
    lightsEven.setColorTemperature(0);
    
    lightsOff.setHue(100);
    lightsEven.setHue(100);
    
    stepOne();
}

def lightsOff() {
	log.debug('Lights off');

	state.isOn = false;
    
    lightsOdd.setColor(hex: '#000000');
    lightsEven.setColor(hex: '#000000');
    
    lightsOff.setHue(0);
    lightsEven.setHue(0);
    
    lightsOdd.setColorTemperature(100);
    lightsEven.setColorTemperature(100);
}

def stepOne() {
	log.debug('Step one...');

	if (state.isOn) {
    	log.debug('is still on.');
    
        lightsOdd.setColor(hex: '#ff0000');
        lightsEven.setColor(hex: '#00ff00');

		unschedule(stepTwo);
        runIn(5, stepTwo);
    }
}

def stepTwo() {
	log.debug('Step two...');

	if (state.isOn) {
    	log.debug('is still on.');
    
        lightsOdd.setColor(hex: '#00ff00');
        lightsEven.setColor(hex: '#ff0000');

		unschedule(stepOne);
        runIn(5, stepOne);
    }
}
