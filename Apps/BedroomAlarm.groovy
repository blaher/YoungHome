definition(
	name: "Bedroom Alarm",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Wakes me up.",
	category: "Convenience",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/rise-and-shine.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/rise-and-shine@2x.png"
);

preferences {
	section('Select Awake Mode') {
		input('awake_mode', 'mode');
	}
    section('Select Asleep Mode') {
		input('asleep_mode', 'mode');
	}
    section('Devices') {
    	input('lights', 'capability.switchLevel', title:'Lights', multiple:true);
    	input('motion', 'capability.motionSensor', title:'Motion Sensor');
    }
}

def installed() {
	log.debug("Installed with settings: ${settings}");
    states();
	schedules();
}

def updated(settings) {
	log.debug('Application updated');
    unsubscribe();
	unschedule();
    states();
	schedules();
}

def states() {
	state.lightLevel = 100;
}

def schedules() {
	log.debug('Scheduling times');

	schedule("0 45 5 ? * 2-6", wakeUp); // Mon - Fri
    schedule("0 0 10 ? * 2-6", checkMotion); // Mon - Fri
}

def wakeUp() {
	log.debug('You\'re about to get up');
    subscribe(motion, 'motion', checkMotion);
}

def checkMotion(evt) {
	log.debug('Good morning');
    
    unsubscribe(motion);
	
    lights.setLevel(100);
    setLocationMode(awake_mode);
    
    runIn(60*1, startLevelUp);
}

def startLevelUp() {
	log.debug('Starting the level up process');

	lights.setLevel(10);
	runIn(60*1, levelUp);
}

def levelUp() {
	log.debug('Leveling up');
    
    state.lightLevel = state.lightLevel + 3;
    lightLevel(state.lightLevel);
    
    unschedule(levelUp);
    
    if (state.lightLevel < 99) {
    	runIn(60*1, levelUp);
    }
}

def lightLevel(value) {
	log.debug('Changing light level');

	state.lightLevel = value;
	lights.setLevel(value);
}
