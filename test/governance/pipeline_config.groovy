keywords {
	master 	= /^[Mm]aster$/
	develop = /^[Dd]evelop$/
	main	= /^[Mm]ain$/
}

@merge libraries {
	sdp {
	  images {
	    registry 	= "https://registry.hub.docker.com"
	    repository 	= "karnc" 
	    cred 	= "docker-hub"
	  }
        }
	git {
	   github
	}
}
