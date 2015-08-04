metadata {
	definition (
		name: 'Virtual Switch',
		namespace: 'younghome',
		author: 'Benjamin J. Young'
	) {
		capability('Switch');
		capability('Refresh');
	}

	simulator {}
	tiles {
		standardTile('button', 'device.switch', canChangeIcon: true, width: 2, height: 2) {
			state('on', label: "${name}", action: 'switch.off', icon: 'st.switches.switch.on', backgroundColor: '#79b821');
			state('off', label: "${name}", action: 'switch.on', icon: 'st.switches.switch.off', backgroundColor: '#ffffff');
		}
		standardTile('refresh', 'device.switch', inactiveLabel: false, decoration: 'flat') {
			state('default', label:'', action:'refresh.refresh', icon:'st.secondary.refresh');
		}        
		main('button');
		details(['button', 'refresh']);
	}
}

def parse(String description) {}

def on() {
	log.debug 'Virtual Switch On';
	sendEvent(name: 'switch', value: 'on');
}

def off() {
	log.debug 'Virtual Switch Off';
	sendEvent(name: 'switch', value: 'off');
}
