void call() {
	stage("Build: Maven") {
		ArrayList<String> phases
    ArrayList<String> goals
    Map<String, String> properties
    ArrayList<String> profiles
    String stage_name = ""
    
    
    
    maven.run(phases,goals,properties,profiles,stage_name)
  }
}
      
