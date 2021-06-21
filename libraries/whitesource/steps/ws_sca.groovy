void call(){
    node{
		
    	stage("WhiteSource: Software Composition Analysis"){
		String product = config.ws_product
                String project = config.ws_project
                String options = config.ws_cli

		String configs = resource(config.ws_config)
	
		sh 'curl -LO https://github.com/whitesource/unified-agent-distribution/releases/latest/download/wss-unified-agent.jar > wss-unified-agent.jar'
  		sh 'curl -LO https://github.com/whitesource/unified-agent-distribution/raw/master/standAlone/wss-unified-agent.config > wss-unified-agent.config'
		
		sh "chmod +x ./wss-unified-agent.config"
		sh "echo \"${configs}\" >> wss-unified-agent.config"
		println "SCA of ${product}/${project}:"
	
		withCredentials([string(credentialsId: 'api_key', variable: 'api_key'), string(credentialsId: 'user_key', variable: ' user_key')]) {
                        sh "java -jar wss-unified-agent.jar -apiKey 'api_key' -userKey 'user_key' -product ${product} -project ${product} -wss.url https://app.whitesourcesoftware.com/agent -d ./. ${options}"
                }
	}
    }
}
