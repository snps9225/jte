
void call() {
	stage("IaC Scan: Checkov") {
		node {
		    String format      = ""
		    String file        = "" 
		    String framework   = "" 
		    String baseline    = "" 
		    String incremental = ""
		    String break_build = ""
		    String script      = ""
		    int flag           = 0

		    format 	= config.Format
		    file        = config.Report
		    framework   = config.Framework
		    baseline    = config.Create_Baseline 
		    incremental = config.Use_Baseline 
		    break_build	= config.Break_Build

		    unstash name: 'maven_build' 

		    test = "find . -type f -name \'\*.\*\' | sed \'s|.\*\.||\' | sort -u > presence"
		    sh test

		    def lines = presence.readLines()
		    lines.each { String line ->
			if(line.contins("tf") || line.contins("yaml") || line.contins("yml")) {
			    flag = 1
			}
		    }

		    if(flag == 1) { 

			if(!config.Format) {
			    format = "json"
			    println "No report format was provided. Default is: " + format
			}

			if(!config.File) {
			  file = "iac-scan-results.json"  
			  println "No report name was provided. Default is: " + file
			}

			if(!config.Framework) {
			    framework = "all"
			    println "No specific framework was selected. Default is: " + framework
			}

			script = 'checkov --directory .'
			script = script + ' --output ' + format + ' > ' + file
			script = script + ' --framework ' + framework 

			if (!config.Create_Baseline || create_baseline.equals("yes")) {
			    script = script + ' --create-baseline'
			}

			if (use_baseline.equals("yes")) {
			    script = script + ' --baseline'
			}
			  
			if (break_build.equals("yes")) {
			    script = script + ' --hard-fail-on'
			}

			sh script
		    }
		    else 
			println "Info: IaC files do not exist. Checkov scanning will be skipped."
        	}
    	}
}
