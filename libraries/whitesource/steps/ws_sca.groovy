void call(){
    node{
		
    	stage("WhiteSource: Software Composition Analysis"){
		String product = config.ws_product
        	String project = config.ws_project
		String configs = resource(config.ws_config)
	
		//sh "echo ${resource(config.ws_config)}"
		sh "echo \"Test Script 2\" > config"  	
		sh "cat ${configs}  >> config"
		println "Contents of config: "
		sh "cat config"
		println "SCA of ${product}/${project} from WS library"
    	}
    }
}
