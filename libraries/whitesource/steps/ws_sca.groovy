void call(){
    node{
		
    	stage("WhiteSource: Software Composition Analysis"){
		String product = config.ws_product
        	String project = config.ws_project
		String configs = resource(config.ws_config)
		String options = config.ws_cli
	
		sh "echo \"Test Script: 2\" > config"
		sh "echo \"${configs}\" >> config"
		sh "cat config"
		println "SCA of ${product}/${project} from WS library"i

		println "s ./wss_agent.sh -apiKey -userKey -product ${product} -project ${project} -wss.url https://app.whitesourcesoftware.com/agent -d ./. ${options}"
    	}
    }
}
