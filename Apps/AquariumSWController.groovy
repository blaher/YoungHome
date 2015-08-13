definition(
	name: 'Aquarium SW Controller',
	namespace: 'younghome',
	author: 'Benjamin J. Young',
	description: 'Controls Saltwater Aquarium Schedule',
	category: 'Convenience',
	iconUrl: 'https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet.png',
	iconX2Url: 'https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet@2x.png'
);

preferences {
	section('Select switch...') { // AquairumSWStrip
		input(name: 'strip', type: 'capability.switch', multiple: false);
	}
}

def installed() {
	log.debug('Installed with settings: ${settings}');
	scheduleTimes();
	startUsed();
}

def updated(settings) {
	log.debug('Application updated');
	unschedule();
	scheduleTimes();
	startUsed();
}

def scheduleTimes() {
	log.debug('Scheduling times');

	schedule('0 0 * * * ?', checkTime);
}

def checkTime() {
	def calendar = Calendar.getInstance();
	def day = calendar.get(Calendar.DAY_OF_WEEK);
	def hour = (new Date(now())).format('HH', location.timeZone);
	
	switch (hour) {
		case 0:
			stopFrontLight();
			stopBackLight();
			startUsed();
		break;
		case 6:
			switch (day){
				case Calendar.MONDAY:
				case Calendar.TUESDAY:
				case Calendar.WEDNESDAY:
				case Calendar.THURSDAY:
				case Calendar.FRIDAY:
					startBackLight();
				break;
			}
		break;
		case 10:
			switch (day){
				case Calendar.MONDAY:
				case Calendar.TUESDAY:
				case Calendar.WEDNESDAY:
				case Calendar.THURSDAY:
				case Calendar.FRIDAY:
					stopBackLight();
				break;
				case Calendar.SATURDAY:
				case Calendar.SUNDAY:
					startBackLight();
				break;
			}
		break;
		case 15:
			switch (day){
				case Calendar.SUNDAY:
					startFrontLight();
				break;
			}
		break;
		case 16:
			startFrontLight();
			startBackLight();
		break;
		case 17:
			switch (day){
				case Calendar.SUNDAY:
					stopBackLight();
				break;
			}
		break;
		case 18:
			switch (day){
				case Calendar.MONDAY:
				case Calendar.TUESDAY:
				case Calendar.WEDNESDAY:
				case Calendar.THURSDAY:
				case Calendar.SATURDAY:
					stopBackLight();
				break;
			}
		break;
		case 20:
			switch (day){
				case Calendar.FRIDAY:
					stopBackLight();
				break;
			}
		break;
		case 22:
			switch (day){
				case Calendar.MONDAY:
				case Calendar.TUESDAY:
				case Calendar.WEDNESDAY:
				case Calendar.THURSDAY:
				case Calendar.SUNDAY:
					stopFrontLight();
				break;
			}
		break;
	}
}

def startFrontLight() {
	log.debug('Turning on front light');
	strip.on1();
}

def stopFrontLight() {
	log.debug('Turning off front light');
	strip.off1();
	startUsed();
}

def startBackLight() {
	log.debug('Turning on back light');
	strip.on2();
}

def stopBackLight() {
	log.debug('Turning off back light');
	strip.off2();
	startUsed();
}

def startUsed() { // Used for other outlets for later use
	log.debug('Turning on used outlets');
	strip.on3();
	strip.on4();
}
