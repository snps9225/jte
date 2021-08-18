void call() {
	stage("Build: Maven") {
		ArrayList<String> phases
		ArrayList<String> goals
		Map<String, String> properties
		ArrayList<String> profiles
		String stage_name = ""
    
		phases	= config.maven.Phases
		goals	= config.maven.Goals 
		properties = config.maven.Properties
		profiles = config.maven.Profile
		stage_name = config.maven.Stage
    
    
		maven.run(phases,goals,properties,profiles,stage_name)
  	}
}
      
