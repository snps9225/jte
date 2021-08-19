void call() {
	stage("Image Scan: Trivy") {
		node {
			String image_name = ""
			String script 	= ""
			String severity = ""
			String flag	= ""
			int break_build = 0
			int index	= 0
			int scanID 	= 1
			
			image_name 	= config.Image_Name 
			break_build	= config.Break_Build
			severity 	= config.Severity
			
			unstash name: 'maven_build' 
			
			flag = sh(script: '''
			touch presence
			find . -type f -name 'Dockerfile' | sed \'s|\\(.*\\)/.*|\\1|\' | sort -u >> presence
			[ -s presence ] && echo 0 || echo 1
			''', returnStdout: true).trim()

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
				
				String [] lines = readFile("presence").split(System.getProperty("line.separator"));

				while (index < lines.length){
					script = "docker build -t \"${image_name}\" \"${lines[index]}\""
					def statusCode = sh script:script, returnStatus:true

					println "trivy image --format json -o image-scan-${scanID}-.json --ignore-unfixed --no-progress --exit-code ${break_build} --severity ${severity} ${image_name}"
					sh "docker rmi \"${image_name}\""
					
					index++
					scanID++
				}
				//archiveArtifacts artifacts: "**/trivy-scan.json"
				sh 'rm presence'
			}
			else
				println "Info: Dockerfile does not exist. Trivy scanning will be skipped."
		}
    	}
}
