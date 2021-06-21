void call(){
    node{
		
    	stage("WhiteSource: Download WS Agent"){
		String product = config.ws_product
        	String project = config.ws_project
		String configs = resource(config.ws_config)
		String options = config.ws_cli
		String agent   = resource("wss_agent")
	
		sh 'curl -LO https://github.com/whitesource/unified-agent-distribution/releases/latest/download/wss-unified-agent.jar > wss-unified-agent.jar'
  		sh 'curl -LO https://github.com/whitesource/unified-agent-distribution/raw/master/standAlone/wss-unified-agent.config > wss-unified-agent.config'
		
		sh "chmod +x ./wss-unified-agent.config"
		sh "echo \"${configs}\" >> wss-unified-agent.config"
		sh "cat wss-unified-agent.config"
		println "SCA of ${product}/${project}:"
	}

	stage("WhiteSource: Run Unified Agent"){
		sh "echo \"${agent}\" > wss_agent.sh"
    		sh "chmod +x ./wss_agent.sh"
		println "s ./wss_agent.sh -apiKey -userKey -product ${product} -project ${project} -wss.url https://app.whitesourcesoftware.com/agent -d ./. ${options}"
    	}
    }
}
