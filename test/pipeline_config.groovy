keywords {
	master 	= /^[Mm]aster$/
	develop = /^[Dd]evelop$/
	main	= /^[Mm]ain$/
}

libraries {
	@override sdp {
		images {
			registry 	= "https://registry.hub.docker.com"
			repository 	= "karnc" 
			cred 		= "docker-hub"
	  	}
        }
	
	@merge checkmarx {
	   	CxServer = "https://checkmarx.corp.n-able.com"
		@override ProjectName = "CxServer\\SP\\Solarwinds\\MSP_N-Central\\General_Scan"
		@override Language = "Java"
		@override CxCred   = "Cx-Access"
   	}
	
	@merge whitesource {
		Api_Key = "api_key"
		Wss_Url = "https://app.whitesourcesoftware.com/agent"
		@override Product  = "WhiteSource_General_Scan"
		@override Project  = "General_Scan"
		@override User_Key = "user_key"
   	}

	@merge trivy {
        	@override Image_Name  = "vuln-check:image"
   	}
	
	/*git {
	   	github
	}*/
	github {
     		source_type = "github"
  	}
}
