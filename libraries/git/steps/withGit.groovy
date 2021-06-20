
void call(){
  stage("Git: Checkout")  {
  String url = config.git_url
  String cred = config.git_cred
  
  // check required parameters
  if (!url || !cred)
    error """
    withGit syntax error.
    Input Parameters:
      url: https git url to repository (required)
      cred: jenkins credential ID for github. (required)
    """

   steps {
      git credentialsId: '${cred}', url: '${url}'
   }
  }
}
