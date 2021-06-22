jte{ 
    allow_scm_jenkinsfile = true
}


@merge libraries {
	sdp {
	  images {
	    registry = "https://docker.pkg.github.com"
	    repository = "boozallen/sdp-images"
	    cred = "public-github"
	    docker_args = "-u 0:0"
	  }
        }
	github {
	   source_type = "github"
	}

	maven {
	   mavenID = "maven"
	}   
	whitesource {
	   Product = "WhiteSource_Test" 
	   Project = "Test_jenkins"
	   Package_Manager = "maven"
    }
}
