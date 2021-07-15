void call() {
} 

void runs(String stageName, Map params = [:], ArrayList<String> phases) {
    this.runs(stageName, params.get('properties', [:]) as Map<String, String>, phases, params.get('goals', []) as ArrayList<String>, params.get('profiles', []) as ArrayList<String>)
}

// Run maven with the image pulled from registry
void runs(String stageName, Map<String, String> properties, ArrayList<String> phases, ArrayList<String> goals, ArrayList<String> profiles) {
       stage("Maven: " + stageName) 
       {  
	     	
	     	// Parsing AWS CodeArtifact	
	     	String aws_configure_cmd = null
	     	String aws_codeartifact_cmd = null 
	        String codeArtifactOutput = null
	  	
	     	if (config.aws.defaultregion) 
	     	{
        		aws_configure_cmd = "aws configure set default.region "+config.aws.defaultregion
    		}
    		
    		if(config.aws.codeartifact_action)
    		{
    			aws_codeartifact_cmd = "aws codeartifact " + config.aws.codeartifact_action
    		}
    		
    		if(config.aws.codeartifact_domain)
    		{
    			aws_codeartifact_cmd = aws_codeartifact_cmd + " --domain "+config.aws.codeartifact_domain
    		}
    		
    		if(config.aws.codeartifact_domain_owner)
    		{
    			aws_codeartifact_cmd = aws_codeartifact_cmd + " --domain-owner "+config.aws.codeartifact_domain_owner
    		}
    		
    		if(config.aws.codeartifact_query)
    		{
    			aws_codeartifact_cmd = aws_codeartifact_cmd + " --query "+config.aws.codeartifact_query
    		}
    		
    		if(config.aws.codeartifact_output)
    		{
    			aws_codeartifact_cmd = aws_codeartifact_cmd + " --output "+config.aws.codeartifact_output
    		}
    		
	     	
	        // Parsing maven code
	        String mvn_command = "mvn "
	        String tag = ""
	        String image_name = "build-tools_maven"
	       
	        if (!config.maven.version) {
        		tag = "381"
    		}
	        else {  
	        	tag = config.maven.version
		}
	        tag = tag.replaceAll("\\.", "")
	        image_name = image_name+tag
	        println("Image name: ", image_name)
	        //image_name = "build-tools_maven381"
		
	        if (!phases) {
			error "Must supply phase for Maven"
		}
		phases.each { phase -> mvn_command += "${phase} "}
		
	        if (goals) {
			goals.each { goal -> mvn_command += "${goal} " }
		}

		if (properties) {
			properties.each { propertyName, value -> mvn_command += "-D${propertyName}"
				if (value != null) {
					mvn_command += "=${value} "
				}
			}
		}
		
		if (profiles) {
			mvn_command += "-P"
			String joined = profiles.join(",")
			mvn_command += joined
		}
	       
	       if(config.maven.modules)
	       {
		      mvn_command += "-pl "
		      mvn_command += config.maven.modules+" "
	       }
	       
	       if(config.maven.settings)
	       {
		      mvn_command += "-s "
		      mvn_command += config.maven.settings+" "
	       }
	       
		inside_sdp_image "maven:3.8.1", {
			
			echo aws_configure_cmd
			echo aws_codeartifact_cmd
			echo mvn_command
			sh mvn_command
			if(stageName == "test") {
				//junit testResults: "${mavenProjectName}/target/surefire-reports/*.xml", allowEmptyResults: true
				println("Print: Running junit ", stageName)
			}
			if(aws_configure_cmd!=null) {
				//sh aws_configure_cmd
			}
			if(aws_codeartifact_cmd!=null) {
				//codeArtifactOutput = sh(script: aws_codeartifact_cmd, returnStdout: true).trim()
			}
			
			//sh "export CODEARTIFACT_AUTH_TOKEN=${codeArtifactOutput} && ${mvn_command} && printenv | sort"
			
		}
      }
}
