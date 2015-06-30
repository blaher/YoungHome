definition(
	name: "Aquarium Topoff Controller",
	namespace: "younghome",
	author: "Benjamin J. Young",
	description: "Controls Aquarium Topoff",
	category: "Pets",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet@2x.png"
);

preferences {
}

def installed() {
	log.debug "Installed with settings: ${settings}";
}

def updated(settings) {
}
