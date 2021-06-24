keywords {
	master 	= /^[Mm]aster$/
	develop = /^[Dd]evelop$/
	main	= /^[Mm]ain$/
}

@merge libraries {
	sdp {
	  images {
	    registry 	= "https://registry.hub.docker.com" //"https://docker.pkg.github.com" //
	    repository 	= "karnc" //"boozallen/sdp-images" 
	    cred 	= "docker-hub"
	    //docker_args = "-u 0:0"
	  }
        }
	git {
	   github
	}
}
