void call() {}

void run(String package_manager) {
	
    node {
		
    	stage("WhiteSource: Software Composition Analysis") {
		unstash "workspace"
		String product 	= ""
                String project 	= ""
                String configs 	= ""
		String script 	= ""
		String ApiKey 	= ""
		String UserKey 	= ""
		String WssUrl 	= ""
		ArrayList custom_config = config.Custom_ConfigOptions
		product	= config.Product
		project = config.Project
		configs = resource(package_manager)
		ApiKey 	= config.Api_Key
		UserKey = config.User_Key
		WssUrl 	= config.Wss_Url
		
		script = 'java -jar /opt/wss-unified-agent.jar'
		sh "echo \"${script}\""
		
		inside_sdp_image "whitesource:openjdk-8", {
			
			dir("${WORKSPACE}") {
				//Add Package Manager Configurations
				//sh "chmod +x ./wss-unified-agent.config"
				sh "echo \"${configs}\" >> /opt/wss-unified-agent.config"

				//Add Additional Configurations
				custom_config.each {
					sh "echo \"${it}\" >> /opt/wss-unified-agent.config"
				}	
				
				withChecks('Whitesource Scan') {
					//Run Unified Agent for SCA
					withCredentials([string(credentialsId: ApiKey, variable: 'api_key'), string(credentialsId: UserKey, variable: 'user_key')]) {
						script = script + ' -apiKey ' + "$api_key" + ' -userKey ' + "$user_key" + ' -product ' + product + ' -project ' + project
						script = script + ' -wss.url ' + WssUrl + ' -d ./. -generateScanReport true'
						def statusCode = sh script:script, returnStatus:true
						if(statusCode==0) {
							sh "cp **/*scan_report.json ${WORKSPACE}"
							archiveArtifacts artifacts: "**/*scan_report.json"
						}
					}
				}
			}
		}
	}
    }
}
