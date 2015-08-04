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
}

def installed() {
	log.debug("Installed with settings: ${settings}");
	scheduleaTimes();
}

def updated(settings) {
	log.debug('Application updated');
	unschedule();
	scheduleTimes();
}

def scheduleTimes() {
	log.debug('Scheduling times');

	schedule("0 0 6 ? * 2-6", wakeUp); // Mon - Fri
    schedule("0 30 21 ? * 1-5", gotoSleep); // Sun - Thur
}

def wakeUp() {
	log.debug('Good Morning.');
	setLocationMode(awake_mode);
}

def gotoSleep() {
	log.debug('Good Night.');
	setLocationMode(asleep_mode);
}
