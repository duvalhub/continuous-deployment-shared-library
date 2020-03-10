import com.duvalhub.writecompose.WriteComposeRequest
import com.duvalhub.deploy.DeployRequest
import com.duvalhub.appconfig.AppConfig

def call(DeployRequest request) {
  echo "Writing Compose file: DeployRequest: '${request.toString()}'"
  List<String> envs = [
    "STACK_NAME=${request.stackName}",
    "APP_NAME=${request.appName}",
    "IMAGE=${request.image}",
    "HOSTS=${request.domainNames}",
    "VOLUMES=${request.volumes}"
  ]
  if (request.deployPort) {
    envs.add("PORT=${request.deployPort}")
  }
  
  String env_files_id = request.getEnvironmentFileId()
  if(env_files_id) {
    withCredentials([file(credentialsId: env_files_id, variable: 'FILE')]) {
      String env_file_content = sh(returnStdout: true, script: 'cat $FILE').trim()
      echo env_file_content
    }
  }

  sh "exit 0"

  withEnv(envs) {
    def processScript = "${env.PIPELINE_WORKDIR}/${request.scriptPath}"
    def compose = request.compose
    executeScript(processScript, false, compose)
    return compose

  }
}

