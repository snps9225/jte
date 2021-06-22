jte{ 
    allow_scm_jenkinsfile = true
}

keywords {
	master 	= /^[Mm]aster$/
	develop = /^[Dd]evelop(ment
	main	= /^[Mm]ain$/
}

@merge libraries {
	sdp {
	  images {
	    registry 	= "https://docker.pkg.github.com"
	    repository 	= "boozallen/sdp-images"
	    cred 	= "public-github"
	    docker_args = "-u 0:0"
	  }
        }
	git {
	   github
	}
}
