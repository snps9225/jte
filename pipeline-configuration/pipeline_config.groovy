keywords {
	master 	= /^[Mm]aster$/
	develop = /^[Dd]evelop$/
	main	= /^[Mm]ain$/
}

@merge libraries {
	sdp {
	  images {
	    registry 	= "https://hub.docker.com"//"https://docker.pkg.github.com" //
	    repository 	= "karnc/maven:3.8.1-openjdk-8"//"boozallen/sdp-images" //"karnc/maven:3.8.1-openjdk-8"
	    //cred 	= "public-github"
	    //docker_args = "-u 0:0"
	  }
        }
	git {
	   github
	}
}
