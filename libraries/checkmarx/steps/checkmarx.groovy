void call() {
	stage("Checkmarx: SAST") 
	{

		String project = ""
		String lang = ""
		String preset = ""
		String LocationPath= ""
		String SASTHigh = ""
		String SASTMedium = ""
		String script = ""
		String report = "cx_output.xml"
		String CxServer= ""
		String LocationType="folder"
		String cred = ""
		
		String report_location = "/opt/CxConsolePlugin/Checkmarx/Reports/"
		
		if (!config.ProjectName) 
		{
        		error "Error : Must provide ProjectName"
    		}
		project = config.ProjectName
		project = project.replace("\\", "\\\\")
	        
		if (!config.Language) 
		{
        		error "Error : Must provide programming language name"
    		}
		lang = config.Language
		lang=lang.toLowerCase()
		
		if (!config.CxServer) 
		{
        		error "Error : Must provide CX Server URL"
    		}
		CxServer = config.CxServer
		
		preset = choose_preset(lang)
		if(preset==null)
		{
			error "Error : Unsupported language template"
		}
		
		if(!config.SASTHigh)
		{
			SASTHigh="5"
		}
		else
		{
			SASTHigh=config.SASTHigh
		}
		if(!config.SASTMedium)
		{
			SASTMedium="5"
		}
		else
		{
			SASTMedium=config.SASTMedium
		}
		
		script = '/opt/CxConsolePlugin/runCxConsole.sh scan -v'
		script = script + ' -ProjectName '+ '\\"'+ project +'\\"'
		script = script + ' -CxServer '+'\\"'+CxServer+'\\"'
		script = script + ' -LocationType '+'\\"'+LocationType+'\\"'
		script = script + ' -SASTHigh '+'\\"'+ SASTHigh +'\\"'
		script = script + ' -SASTMedium '+'\\"'+SASTMedium+'\\"'
		script = script + ' -Preset '+ '\\"'+preset+'\\"'
		script = script + ' -ReportXML '+'\\"'+report+'\\"'
		script = script + ' -Incremental'
		
		// Fix the Dockerfile
		// Archive the results
		cred = config.CxCred
         	inside_sdp_image "checkmarx:openjdk-8", {
         	 	
			dir("${WORKSPACE}")
			{

				script = script + ' -LocationPath '+'\\"'+ "${WORKSPACE}" +'\\"'
				withChecks('Checkmarx Scan') 
				{
					withCredentials([usernamePassword(credentialsId: cred, passwordVariable: 'pass', usernameVariable: 'uname')]) 
					{
						script = script + ' -CxUser '+'\\"'+"$uname"+'\\"'+' -CxPassword '+'\\"'+"$pass"+'\\"'
						def statusCode = sh script:script, returnStatus:true
						
						if(statusCode==0 || statusCode>5)
						{
							//stash includes: '/opt/CxConsolePlugin/Checkmarx/Reports/cx_output.xml', name: 'Cxscan'
							sh "cp /opt/CxConsolePlugin/Checkmarx/Reports/cx_output.xml ${WORKSPACE}"
							archiveArtifacts artifacts: report
						}
					}
				}
			}
		}
		
	}
}

String choose_preset(String lang)
{
	switch(lang)
	{
		case "java":
			return '\"Java - OWASP Top 10\"'
		default:
			return null
	}
}
