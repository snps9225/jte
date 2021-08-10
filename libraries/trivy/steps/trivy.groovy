void call() {
	stage("Trivy: Image Scan") {
		node {
			String opt_in	= ""
			String image_name = ""
			String script 	= ""
			int break_build = 0
			String severity = ""
			String flag	= ""
			opt_in 		= config.Opt_In
			image_name 	= config.Image_Name 
			break_build	= config.Break_Build
			severity 	= config.Severity
			
			if(!config.Opt_In || config.Opt_In.toLowerCase() == 'yes') {
				unstash name: 'maven_build'  
				//sh "pwd"
				
				//flag = sh "find -name Dockerfile"
				//println "File Present: " + flag
				//sh "test -e Dockerfile && echo \"1\">flag || echo \"0\">flag"
				//sh "ls -la"
				//flag = sh "test -e Dockerfile && echo \"1\" || echo \"0\""
				
				flag = sh(script: "test -e Dockerfile && echo \"1\" || echo \"0\"", returnStdout: true)
				println "value of flag is: "+flag
				if(flag=='1') {
					println "Dockerfile exists"
				
					if(!config.Image_Name) {
						image_name = "vuln-scan:trivy"
						println "No image name was provided. Default image name to be scanned is, " + image_name
					}

					if(!config.Break_Build) {
						println "Selected default break build setting: Do not break build"
						break_build = 0
					}

					if(!config.Severity) {
						println "Selected default severity setting: High and Critical"
						severity = "HIGH,CRITICAL"
					}

					script = 'docker build -t ' + image_name + ' .'
					sh script
					//Runs scan here
					//archiveArtifacts artifacts: "**/trivy-scan.json"
				}
				else
					println "Dockerfile does not exist"
			}
			
			else
				println "Trivy Image Scanning was opted out." 
		}
    	}
}
