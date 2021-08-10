void call() {
	stage("Trivy: Image Scan") {
		node {
			String opt_in	= ""
			String image_name = ""
			String script 	= ""
			int break_build = 0
			String severity = ""
			String test	= ""
			String flag	= ""
			image_name 	= config.Image_Name 
			break_build	= config.Break_Build
			severity 	= config.Severity
			
			unstash name: 'maven_build' 

			test = "test -e Dockerfile && echo \"1\" || echo \"0\""
			flag = sh(script: test, returnStdout: true).trim()

			if(flag.equals("1")) {

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
				println "Info: Dockerfile does not exist. Trivy scanning will be skipped."
		}
    	}
}
