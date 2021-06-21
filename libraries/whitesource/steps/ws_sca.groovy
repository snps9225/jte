void call(){
    node{
		
    	stage("WhiteSource: Software Composition Analysis"){
		String product = config.ws_product
        	String project = config.ws_project
		String configs = resource(config.ws_config)
	
		//sh "echo ${resource(config.ws_config)}"
		echo "Test Script 2" > config  	
		//sh "echo \"Test script\" > config1"
		//sh "cat ${configs}  >> configs1"
		println "Contents of config: "
		echo config
		println "SCA of ${product}/${project} from WS library"
		sh 
    	}
    }
}
