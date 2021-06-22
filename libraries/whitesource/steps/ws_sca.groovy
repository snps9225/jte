void call(){
    node("SCA"){
		
    	stage("WhiteSource: Software Composition Analysis"){
		unstash "workspace"
		String product = config.Product
                String project = config.Project
                String configs = resource(config.Package_Manager)
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
			sh "java -jar wss-unified-agent.jar -apiKey \"$api_key\" -userKey \"$user_key\" -product ${product} -project ${product} -wss.url https://app.whitesourcesoftware.com/agent -d ./."
                }
	}
    }
}
