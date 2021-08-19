void call() {
	stage("Image Scan: Trivy") {
		node {
			String image_name = ""
			String script 	= ""
			int break_build = 0
			String severity = ""
			String flag	= ""
			
			image_name 	= config.Image_Name 
			break_build	= config.Break_Build
			severity 	= config.Severity
			
			unstash name: 'maven_build' 
			
			flag = sh(script: '''
			touch presence
			find . -type f -name 'Dockerfile' | sed \'s|\\(.*\\)/.*|\\1|\' | sort -u >> presence
			[ -s presence ] && echo 0 || echo 1
			''', returnStdout: true).trim()
			
			sh 'rm presence'
			
			//test = "test -e Dockerfile && echo 0 || echo 1"
			//flag = sh(script: test, returnStdout: true).trim()

			if(flag.equals("0")) {

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
				
				Scanner s = new Scanner(new File("./presence"))
				ArrayList<String> list = new ArrayList<String>()
				while (s.hasNext()){
				    println "Value: " + s.next()
				}
				s.close()
				
				script = 'docker build -t ' + image_name + ' .'
				//def statusCode = sh script:script, returnStatus:true
	
				//sh script
				
				//archiveArtifacts artifacts: "**/trivy-scan.json"
			}
			else
				println "Info: Dockerfile does not exist. Trivy scanning will be skipped."
		}
    	}
}
