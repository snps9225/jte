keywords {
	master 	= /^[Mm]aster$/
	develop = /^[Dd]evelop$/
	main	= /^[Mm]ain$/
}

@merge libraries {
	@override sdp {
		images {
	    	registry 	= "https://registry.hub.docker.com"
	    	repository 	= "karnc" 
	    	cred 		= "docker-hub"
	  	}
        }
	/*checkmarx {
		Cx_url = https://
	}*/
	git {
	   github
	}
}
