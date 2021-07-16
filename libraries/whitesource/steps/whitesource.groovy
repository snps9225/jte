void call() {}

void run(String package_manager) {
	
    node {
		
    	stage("WhiteSource: Software Composition Analysis"){
		unstash "workspace"
		String product = config.Product
                String project = config.Project
                String configs = resource(package_manager)
		String script = ""
		String ApiKey = ""
		String UserKey = ""
		String WSSURL = ""
		ArrayList custom_config = config.Custom_ConfigOptions
		ApiKey = config.Api_Key
		UserKey = config.User_Key
		WSSURL = config.Wss_Url
		
		//Download Unified Agent and Configuration File
		sh 'curl -LO https://github.com/whitesource/unified-agent-distribution/releases/latest/download/wss-unified-agent.jar > wss-unified-agent.jar'
  		sh 'curl -LO https://github.com/whitesource/unified-agent-distribution/raw/master/standAlone/wss-unified-agent.config > wss-unified-agent.config'
		
		//Add Package Manager Configurations
		sh "chmod +x ./wss-unified-agent.config"
		sh "echo \"${configs}\" >> wss-unified-agent.config"
		
		//Add Additional Configurations
		custom_config.each{
			sh "echo \"${it}\" >> wss-unified-agent.config"
		}		

		//Run Unified Agent for SCA
		withCredentials([string(credentialsId: ApiKey, variable: 'api_key'), string(credentialsId: UserKey, variable: 'user_key')]) {
			script = 'java -jar wss-unified-agent.jar'
			script = script + ' -apiKey ' + "$api_key" + ' -userKey ' + "$user_key" + ' -product ' + product + ' -project ' + project
			script = script + ' -wss.url ' + WSSURL + ' -d ./. -generateScanReport true'
			def statusCode = sh script:script, returnStatus:true
			if(statusCode==0)
				{
					archiveArtifacts artifacts: "**/*scan_report.json"
				}
			//sh "java -jar wss-unified-agent.jar -apiKey \"$api_key\" -userKey \"$user_key\" -product ${product} -project ${project} -wss.url https://app.whitesourcesoftware.com/agent -d ./."
                }
	}
    }
}
