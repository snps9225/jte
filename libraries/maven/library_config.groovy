fields { 
  optional {
	  maven
	  {
	  	version = String
		settings = String
		modules = String
		stage_name = String
		artifact_version = String  
	  }
	  aws
	  {
	  	defaultregion = String
	 	codeartifact_action = String
	  	codeartifact_domain = String
	  	codeartifact_domain_owner = String
	  	codeartifact_query = String
	  	codeartifact_output = String
	  }
  }
}
