void call() {
	stage("Trivy: Image Scan") {
		node {
			String image_name = ""
			String script = "" 
			image_name = config.Image_Name 
			script = 'docker build -t ' + image_name + ' .'
			
			sh "test -w ./Dockerfile2 && echo \"Dockerfile exists.\" || echo \"Dockerfile does not exist.\""
			sh "test -w ./Dockerfile2 && echo \"\n\nUSER root\" >> Dockerfile || echo \"Dockerfile does not exist.\""
			sh "test -w ./Dockerfile2 && echo \"\nRUN curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin && trivy filesystem --exit-code 1 --no-progress /\" >> Dockerfile || echo \"Dockerfile does not exist.\""
			sh "test -w ./Dockerfile2 && sh ${script} || echo \"Dockerfile does not exist.\""
			
			
			/*if(file.exists()) {
				sh "echo \"A Dockerfile exists.\""
				sh "echo \"USER root\" >> Dockerfile"
				sh "echo \"RUN curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin && trivy filesystem --exit-code 1 --no-progress /\" >> Dockerfile"
			}
			else {
				sh "echo \"Dockerfile does not exist.\""
			}

			sh "cat Dockerfile"
			sh script */
		}
    	}
}
