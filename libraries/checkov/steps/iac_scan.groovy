
void call() {
	stage("IaC Scan: Checkov") {
		node {
			String format      = ""
			String file        = "" 
			String framework   = "" 
			String baseline    = "" 
			String use_baseline= ""
			String script      = ""
			String flag        = ""

			format	= config.Format
			file    = config.Report
			framework   = config.Framework
			baseline    = config.Create_Baseline 
			use_baseline= config.Use_Baseline 

			unstash name: 'maven_build' 
			
			flag = sh(script: '''
			touch presence
			find . -type f -name \'*.taf\' | sed \'s|.*\\.||\' | sort -u >> presence
			find . -type f -name \'*.yaaml\' | sed \'s|.*\\.||\' | sort -u >> presence
			[ -s presence ] && echo 0 || echo 1
			''', returnStdout: true).trim()
			
			sh 'rm presence'

			if(flag.equals("0")) { 
				if(!config.Format) {
					format = "json"
				    	println "No report format was provided. Default is: " + format
				}

				if(!config.File) {
					file = "iac-scan-results.json"  
					println "No report name was provided. Default is: " + file
				}

				if(!config.Framework) {
					framework = "all"
					println "No specific framework was selected. Default is: " + framework
				}

				script = 'docker run -t --name checkov -v /var/lib/jenkins/workspace/insecure-bank-mbp_develop:/tf bridgecrew/checkov --directory /tf '
				script = script + ' --output ' + format
				script = script + ' > ' + file
				script = script + ' --framework ' + framework 

				if (!config.Create_Baseline || create_baseline.equals("yes")) {
					script = script + ' --create-baseline'
				}

				if (use_baseline.equals("yes")) {
					script = script + ' --baseline'
				}
				
				def statusCode = sh script:script, returnStatus:true
				
				archiveArtifacts artifacts: "**/${file}"
				
				sh 'docker rm checkov'
			}
			else 
				println "Info: IaC files do not exist. Checkov scanning will be skipped."
        	}
    	}
}
