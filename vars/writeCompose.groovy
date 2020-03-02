import com.duvalhub.writecompose.WriteComposeRequest
import com.duvalhub.appconfig.AppConfig

def call(WriteComposeRequest request) {
  String[] envs = [
    "STACK_NAME=${request.request.getStackName()}",
    "APP_NAME=${request.appName}",
    "IMAGE=${request.getImage()}",
    "HOSTS=${request.getDomainNames()}",
    "VOLUMES=${request.getVolumes()}"
  ]
  if (request.port) {
    envs.add("PORT=${request.port}")
  }
  
  String env_files_id = request.getEnvironmentFileId()

  withCredentials([file(credentialsId: env_files_id, variable: 'FILE')]) {
    String env_file_content = sh(returnStdout: true, script: 'cat $FILE').trim()
    echo env_file_content
  }
  sh "exit 0"


  withEnv(envs) {
    def processScript = "${env.PIPELINE_WORKDIR}/${request.scriptPath}"
    def compose = request.compose
    executeScript(processScript, false, compose)
    return compose

  }
}

