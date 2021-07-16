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
	@merge checkmarx {
		CxServer = "https://checkmarx.corp.n-able.com"
		images {
			cred = "Cx-Access"
		}
	}
	git {
	   github
	}
}
