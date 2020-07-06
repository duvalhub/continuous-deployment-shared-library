import com.duvalhub.deploy.DeployRequest

def call(DeployRequest request) {
  echo "Writing Compose file: DeployRequest: '${request.toString()}'"
  List<String> envs = [
    "STACK_NAME=${request.stackName}",
    "APP_NAME=${request.appName}",
    "IMAGE=${request.getDockerImageFull()}",
    "HOSTS=${request.domainNames}",
    "VOLUMES=${request.volumes}",
    "NETWORKS=${request.networks}"
  ]
  if (request.deployPort) {
    envs.add("PORT=${request.deployPort}")
  }

  String environment_variables
  for (String environment_file_id: request.getEnvironmentFileId()) {
    if(!environment_variables) {
      environment_variables = ""
    }
    withCredentials([file(credentialsId: environment_file_id, variable: 'FILE')]) {
      String env_file_content = sh(returnStdout: true, script: 'cat $FILE').trim()
      environment_variables = environment_variables + env_file_content + '\n'
    }
  }
  if(environment_variables) {
    envs.add("ENV_VARIABLES=${environment_variables}")
  }

  withEnv(envs) {
    def processScript = "${env.PIPELINE_WORKDIR}/${request.scriptPath}"
    def compose = request.compose
    executeScript(processScript, false, compose)
    return compose

  }
}

