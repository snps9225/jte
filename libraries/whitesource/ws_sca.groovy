void call(){
    stage("WhiteSource: Software Composition Analysis"){
	String product = config.ws_product
        String project = config.ws_project
	println "SCA of ${product}/${project} from WS library"
    }
}
