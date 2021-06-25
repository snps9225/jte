void call() {
} 

void run(Map params = [:], ArrayList<String> phases) {
    this.run(phases, params.get('goals', []) as ArrayList<String>, params.get('properties', [:]) as Map<String, String>, params.get('profiles', []) as ArrayList<String>)
}

// Run maven with the image pulled from registry
void run(ArrayList<String> phases, ArrayList<String> goals, Map<String, String> properties, ArrayList<String> profiles) {
       stage("Maven: Build") {  
	       	        
	        String command = "mvn "
	        String tag = ""
	        String image_name = "build-tools/maven_"
	       
	        if (!config.maven_version) {
        		tag = "3.8.1"
    		}
	        else {       
	        	tag = config.maven_version
		}
	        tag = tag.replaceAll("_", ".")
	        image_name = image_name+tag 
	        println image_name
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
	       
		inside_sdp_image "${image_name}", {
	            	sh command
		}
      }
}
