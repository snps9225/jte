void call() {
} 
void run(Map params = [:], ArrayList<String> phases, String stage_name) {
    this.run(phases, params.get('goals', []) as ArrayList<String>, params.get('properties', [:]) as Map<String, String>, params.get('profiles', []) as ArrayList<String>, stage_name)
}
// Run maven with the image pulled from registry
void run(ArrayList<String> phases, ArrayList<String> goals, Map<String, String> properties, ArrayList<String> profiles, String stage_name) {

		stage("Maven: " + stage_name) 
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
			echo image_name
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

		       if(config.maven.artifact_version && phases.contains("deploy"))
		       {
			      mvn_command += "-Dpackage.version=" + config.maven.artifact_version
		       }

			inside_sdp_image "maven:3.8.1", {

				echo aws_configure_cmd
				echo aws_codeartifact_cmd
				echo mvn_command
				sh mvn_command
				//Stash maven build
				//stash includes: '**/*.war, **/*.jar', allowEmpty: true, name: 'maven_build'
				stash name: 'maven_build', allowEmpty: true  

				if(phases.contains("test")) {
					echo "Run JUNIT"
					//junit testResults: "/target/surefire-reports/*.xml", allowEmptyResults: true
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
