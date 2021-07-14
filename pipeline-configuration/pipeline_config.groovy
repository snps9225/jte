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
	checkmarx {
		CxServer = "https://checkmarx.corp.n-able.com"
	}
	git {
	   github
	}
}
