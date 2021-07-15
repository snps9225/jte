void call() {}

void run(String package_manager) {
	
    node {
		
    	stage("WhiteSource: Software Composition Analysis"){
		unstash "workspace"
		String product = config.Product
                String project = config.Project
                String configs = resource(package_manager)
		String script = "java -jar wss-unified-agent.jar"
		ArrayList custom_config = config.Custom_ConfigOptions
		
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
		withCredentials([string(credentialsId: 'api_key', variable: 'api_key'), string(credentialsId: 'user_key', variable: 'user_key')]) {
			script = script + " -apiKey " + $api_key " -userKey " + $user_key + " -product " + product + " -project " + project
			script = script + " -wss.url https://app.whitesourcesoftware.com/agent -d ./. -generateScanReport true"
			def statusCode = sh script:script, returnStatus:true
			if(statusCode==0 || statusCode>5)
				{
					archiveArtifacts artifacts: "**/*scan_report.json"
				}
			//sh "java -jar wss-unified-agent.jar -apiKey \"$api_key\" -userKey \"$user_key\" -product ${product} -project ${project} -wss.url https://app.whitesourcesoftware.com/agent -d ./."
                }
	}
    }
}
