void call() {
	stage("Trivy: Image Scan") {
		node {
			String image_name = ""
			String script = "" 
			//Boolean flag = false
			image_name = config.Image_Name 

			if(sh "[ -f ./Dockerfile ]") {
				sh "echo \"A Dockerfile exists.\""
				sh "echo \"USER root\" >> Dockerfile"
				sh "echo \"RUN curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin && trivy filesystem --exit-code 1 --no-progress /\" >> Dockerfile"
			}
			else {
				sh "echo \"Dockerfile does not exist.\""
			}

			script = 'docker build -t ' + image_name + ' .'  
			sh script
		}
    	}
}
