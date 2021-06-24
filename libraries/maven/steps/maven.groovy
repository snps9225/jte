void call() {
} 

void run(Map params = [:], ArrayList<String> phases) {
    this.run(phases, params.get('goals', []) as ArrayList<String>, params.get('properties', [:]) as Map<String, String>, params.get('profiles', []) as ArrayList<String>)
}

// Run maven with the image pulled from registry
void run(ArrayList<String> phases, ArrayList<String> goals, Map<String, String> properties, ArrayList<String> profiles) {
       stage("Maven: Build") {  
	        unstash "workspace"
	        
	        String command = "mvn "
	        String tag = ""
	       
	        if (!config.image_tag) {
        		tag = "3.8.1-openjdk-8"
    		}
	        else {       
	        	tag = config.image_tag
		}
		if (!phases) {
			error "Must supply phase for Maven"
		}
		phases.each { phase -> command += "${phase} "}
		
	        if (goals) {
			goals.each { goal -> command += "${goal} " }
		}

		if (properties) {
			properties.each { propertyName, value -> command += "-D${propertyName} "
				if (value != null) {
					command += "= ${value} "
				}
			}
		}
		
		if (profiles) {
			command += "-P"
			String joined = profiles.join(",")
			command += joined
		}
	       
		inside_sdp_image "maven:${tag}", {
	            sh command
		}
      }
}
