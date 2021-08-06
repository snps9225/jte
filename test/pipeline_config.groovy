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
	
	whitesource {
		Api_Key = "api_key"
		Wss_Url = "https://app.whitesourcesoftware.com/agent"
	}
	
	/*git {
	   	github
	}*/
	github {
     		source_type = "github"
  	}
}
