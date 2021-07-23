void call() {
	stage("Trivy: Image Scan") {
		node {
			String image_name = ""
			String script = ""
			String report_format = "table"
			int break_build = 0
			image_name = config.Image_Name 
			report_format = config.Report_Format
			break_build = config.Break_Build
			
			script = 'docker build -t ' + image_name + ' . > trivy-result.txt'
			
			//sh "test -w ./Dockerfile && echo \"Dockerfile exists.\" || echo \"Dockerfile does not exist.\""
			sh "test -w ./Dockerfile && echo \"\n\nUSER root\" >> Dockerfile || echo \"\""
			sh "test -w ./Dockerfile && echo \"\nRUN curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin && trivy filesystem --ignore-unfixed --severity HIGH,CRITICAL --format \"${report_format}\" --exit-code \"${break_build}\" --no-progress /\" >> Dockerfile || echo \"\""
			//sh "test -w ./Dockerfile && sh ${script} || echo \"\""

			sh script
	
			archiveArtifacts artifacts: "**/trivy-result.txt"
		}
    	}
}
