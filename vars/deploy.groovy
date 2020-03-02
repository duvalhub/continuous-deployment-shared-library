import com.duvalhub.deploy.DeployRequest
import com.duvalhub.writecompose.WriteComposeRequest

def call(DeployRequest request) {
  stage('Deploy') {
    WriteComposeRequest writeComposeRequest = new WriteComposeRequest(request)
    String composeFilePath = writeCompose(writeComposeRequest)
    setDockerEnvironment.withCredentials(request.getDockerHost(), request.getCredentialId()) {
      String prepare_script = "${env.PIPELINE_WORKDIR}/deploy/scripts/network/createIfNotExist.sh"
      String params = "--network ${request.getInternalNetwork()}"
      executeScript(prepare_script, false, params)

      String deploy_script = "${env.PIPELINE_WORKDIR}/deploy/scripts/deploy/deploy.sh"
      setEnv([
        "COMPOSE_FILE_PATH=${composeFilePath}",
        "STACK_NAME=${request.getStackName()}"
      ]) {
        executeScript(deploy_script)
      }

//      sh "echo \"$DOCKER_CREDENTIALS_PSW\" | docker login --username \"$DOCKER_CREDENTIALS_USR\" --password-stdin && docker stack deploy -c ${composeFilePath} ${request.getStackName()}"
    }
  }
}

