void call() {
	stage("Trivy: Image Scan") {
		node {
			String opt_in	= ""
			String image_name = ""
			String script 	= ""
			String report_format = ""
			int break_build = 0
			String severity = ""
			opt_in 		= config.Opt_In
			image_name 	= config.Image_Name 
			report_format 	= config.Report_Format
			break_build	= config.Break_Build
			severity 	= config.Severity
			
			if(!config.Opt_In || config.Opt_In.toLowerCase() == 'yes') {
			
				if(!config.Report_Format) {
					report_format = "json"

				}

				if(!config.Image_Name) {
					println "No image name was provided. Using a default image name."
					image_name = "vuln-check:test"

				}

				if(!config.Break_Build) {
					break_build = 0

				}

				if(!config.Severity) {
					severity = "HIGH,CRITICAL"

				}
				
				script = 'docker build -t ' + image_name + ' .'
				unstash name: 'maven_build'  
				sh script
				sh "/test/trivy image --format \"${report_format}\" -o trivy-scan.json --ignore-unfixed --no-progress --exit-code \"${break_build}\" --severity \"${severity}\" \"${image_name}\""

				archiveArtifacts artifacts: "**/trivy-result.txt"
			}
			
			else
				println "Trivy Image Scanning was opted out." 
		}
    	}
}
