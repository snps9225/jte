keywords {
	master 	= /^[Mm]aster$/
	develop = /^[Dd]evelop$/
	main	= /^[Mm]ain$/
}

@merge libraries {
/*	sdp {
	  images {
	    registry 	= "https://docker.pkg.github.com" //"https://hub.docker.com"
	    repository 	= "boozallen/sdp-images" //"karnc/maven:3.8.1-openjdk-8"
	    cred 	= "public-github"
	    docker_args = "-u 0:0"
	  }
        }*/
	git {
	   github
	}
}
