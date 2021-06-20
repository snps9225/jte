
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
        withCredentials([usernamePassword(credentialsId: cred, passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        repo = url.split("/").last() - ".git"
        withEnv(["git_url_with_creds=${url.replaceFirst("://","://${USER}:${PASS}@")}"]) {
        node {
        sh "rm -rf ${repo}"
        sh "set +x && git clone ${env.git_url_with_creds}"
        dir(repo){
          sh "git checkout"
          }
      }
    }
   }
  }
}
