definition(
	name: "Bedroom Lights Controller",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Controls Bedroom Lights",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/smartlights.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/smartlights@2x.png"
);

preferences {
	section('Devices') {
		input('lights', 'capability.switchLevel', title:'Lights', multiple:true);
        input('motion', 'capability.motionSensor', title:'Motion Sensor');
        input('illuminance', 'capability.illuminanceMeasurement', title:'Light Sensor');
	}

	section('Modes') {
		input('awake_mode', 'mode', title:'Awake');
		input('sleep_mode', 'mode', title:'Sleep');
		input('away_mode', 'mode', title:'Away');
	}
}

def installed() {
	log.debug("Installed with settings: ${settings}");
    
    states();
    subscribes();
    schedules();
}
    
def updated() {
	log.debug('App updated');

	states();
	unsubscribe();
    subscribes();
    unschedule();
    schedules();
}

def states() {
	log.debug('Defining intial states');
    
    state.currentMode = awake_mode;
    state.previousMode = sleep_mode;
    
    state.currentMotion = 'active';
    
    state.lightStatus = true;
    state.lightTemperature = 'cool';
    state.lightLevel = 100;
    state.lightAuto = true;
    state.lightMin = 400;
    state.lightMax = 500;
}

def subscribes() {
	log.debug('Subscribing to actions');

	subscribe(location, 'mode', checkMode);
    subscribe(motion, 'motion', checkMotion);
    subscribe(lights, 'setLevel', checkLevel);
    subscribe(illuminance, 'illuminance', checkIlluminance);
}

def schedules() {
	log.debug('Scheduling events');

    schedule('0 0 5,17 * * ?', lightLow);
    schedule('0 0 8 * * ?', lightFull);
    
    schedule('0 0 20 * * ?', startDim);
    
}

def checkMode(evt) {
	log.debug('Checking mode');
    
	state.previousMode = state.currentMode;
	state.currentMode = evt.value;
    log.debug("Previous Mode: ${state.previousMode}");
    log.debug("Current Mode: ${state.currentMode}");

	if (state.currentMode == sleep_mode) {
		log.debug('You went to bed');
		lightsOff();
	} else if (state.currentMode == awake_mode && state.previousMode == sleep_mode) {
    	log.debug('You woke up');
		lightsOn();
	} else if (state.currentMode == away_mode) {
    	log.debug('You left the house');
		lightsOff();
	} else if (state.currentMode == awake_mode && state.previousMode == away_mode) {
    	log.debug('You came home');
		lightsOn();
	}
}

def checkMotion(evt) {
	log.debug('Checking motion event');
	state.currentMotion = evt.value;

	if(evt.value == 'active' && state.currentMode == awake_mode) {
		log.debug('You moved');
    	lightsOn();
    } else if(evt.value == 'inactive' && state.currentMode == awake_mode) {
		log.debug('No more movement');
        unschedule(recheckMotion);
    	runIn(60*15, recheckMotion);
    }
}

def recheckMotion(evt) {
	log.debug('Rechecking motion');
    
	if (state.currentMotion == 'inactive') {
		log.debug('Looks like you are no longer in the room');
		lightsOff();
    }
}

def checkLevel(evt) {
	log.debug('Checking level');
    
	lightLevel(evt.value, false);
}

def checkIlluminance(evt) {
	log.debug('Checking Illuminance');

	if (state.lightAuto == true) {
        log.debug("Light level before: ${state.lightLevel}");
        if (evt.integerValue <= 100) {
            log.debug('Way too dark');
            state.lightLevel = 100;
        } else if (evt.integerValue < state.lightMin && state.lightLevel < 100) {
            log.debug('Too dark');
            state.lightLevel = state.lightLevel + 5;
        } else if (evt.integerValue > state.lightMax && state.lightLevel > 0) {
            log.debug('Too Bright');
            state.lightLevel = state.lightLevel - 5;
        }
        log.debug("Light level after: ${state.lightLevel}");
        
        lightLevel(state.lightLevel, true);
    }
}

def lightTemperature(value) {
	log.debug("Changing light temperature to ${value}");

	state.lightTemperature = value;
    
    if (value == "warm") {
    	lights.setColorTemperature(100);
    } else {
    	lights.setColorTemperature(0);
    }
}

def startDim() {
	log.debug('Starting night time dim schedule');

	lightLevel(100, false);
    runIn(60*1, dim);
}

def dim() {
	log.debug('Dimming');

	log.debug("Light level before: ${state.lightLevel}");
	state.lightLevel = state.lightLevel - 1;
    log.debug("Light level after: ${state.lightLevel}");
    
    if (state.lightLevel <= 10) {
    	log.debug('Lowest light level reached');
    	stopDim();
    } else {
    	lightLevel(state.lightLevel, false);
        unschedule(dim);
        runIn(60*1, dim);
    }
}

def stopDim() {
	log.debug('Stopping night time dim schedule');

	unschedule(dim);
	lightLevel(10, false);
    
    state.lightAuto = true;
    log.debug('Now allowing lux sensor to change light');
}

def lightLevel(level, auto = true) {
	log.debug("Checking if light level can be changed: ${state.lightAuto}, ${auto}");

	if (state.lightAuto == true || auto == false) {
    	log.debug("Changing light level to ${level}");
		state.lightLevel = level;
        
        if (state.lightStatus == true) {
        	log.debug('Sending light level change');
        	lights.setLevel(state.lightLevel);
        }
    }
    
    if (auto == false) {
    	log.debug('Disallowing lux sensor to change light');
    	state.lightAuto = false;
    }
}

def lightsOn() {
	log.debug('Turning lights on');

	state.lightStatus = true;
	lights.on();
    lightLevel(state.lightLevel);
}

def lightsOff() {
	log.debug('Turning lights off');

	state.lightStatus = false;
	lights.off();
}

def lightLow() {
	log.debug('Toning down the lights');

	lightTemperature('warm');    
    state.lightMin = 300;
    state.lightMax = 400;
    
    if (state.lightLevel < 50) {
    	lightLevel(50);
    }
}

def lightFull() {
	log.debug('Toning up the lights');

	lightTemperature('cool');    
    state.lightMin = 400;
    state.lightMax = 500;
    
    if (state.lightLevel > 50) {
    	lightLevel(50);
    }
}
