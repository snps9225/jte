void call() {
	stage("Trivy: Image Scan") {
		String image_name = ""
		String script = "" 
		boolean flag = false
		image_name = config.Image_Name 

		unstash "workspace" 

		sh "[ -f ./Dockerfile ] && echo \"${configs}\"=true && echo \"Dockerfile exists.\""

		if(flag==true) {
		    sh "echo \"USER root\" >> Dockerfile"
		    sh "echo \"RUN curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin && trivy filesystem --exit-code 1 --no-progress /\" >> Dockerfile"
		}

		script = 'docker build -t ' + image_name + ' .'  
		sh script 
    	}
}
