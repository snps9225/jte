import java.util.logging.Logger


void call() {
	stage("Trivy: Image Scan") {
		node {

			Logger logger = Logger.getLogger("")
			
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
			
				if(!config.Image_Name) {
					image_name = "vuln-scan:trivy"
					info "No image name was provided. Default image name to be scanned is, " + image_name
				}
				
				if(!config.Report_Format) {
					info "Selected default output result: JSON"
					report_format = "json"
				}

				if(!config.Break_Build) {
					info "Selected default break build setting: Do not break build"
					break_build = 0
				}

				if(!config.Severity) {
					info "Selected default severity setting: High and Critical"
					severity = "HIGH,CRITICAL"
				}
				
				script = 'docker build -t ' + image_name + ' .'
				unstash name: 'maven_build'  
				sh script
				sh "/test/trivy image --format \"${report_format}\" -o trivy-scan.json --ignore-unfixed --no-progress --exit-code \"${break_build}\" --severity \"${severity}\" \"${image_name}\""

				archiveArtifacts artifacts: "**/trivy-result.txt"
			}
			
			else
				logger.info ("Trivy Image Scanning was opted out.") 
		}
    	}
}
