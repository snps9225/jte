void call() {

} 

void run(Map params = [:], ArrayList<String> phases) {
    if (!config.mavenId) {
        error "Must supply the installed Maven version's ID"
    }
    this.run(phases, params.get('goals', []) as ArrayList<String>, params.get('properties', [:]) as Map<String, String>, params.get('profiles', []) as ArrayList<String>)
}

// Run maven command with maven image pulled from registry
// Run maven command with maven image pulled from registry
void run(ArrayList<String> phases, ArrayList<String> goals, Map<String, String> properties, ArrayList<String> profiles) {
       stage("Maven") {  // run mvn command in maven container
		String command = "echo DEBUG "
		if (properties) {
			command += properties.get('image_tag')
	    	}
	        sh command
       }
}