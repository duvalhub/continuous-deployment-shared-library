import com.duvalhub.deploy.DeployRequest
import com.duvalhub.writecompose.WriteComposeRequest

def call(DeployRequest request) {
  stage('Deploy') {
    WriteComposeRequest writeComposeRequest = new WriteComposeRequest(request)
    String composeFilePath = writeCompose(writeComposeRequest)
    setDockerEnvironment.withCredentials(request.getDockerHost(), request.getCredentialId()) {
      String script = "${env.PIPELINE_WORKDIR}/deploy/scripts/network/createIfNotExist.sh"
      String params = "--network ${request.getInternalNetwork()}"
      executeScript(script, false, params)
      sh "echo \"$DOCKER_CREDENTIALS_PSW\" | docker login --username \"$DOCKER_CREDENTIALS_USR\" --password-stdin && docker stack deploy -c ${composeFilePath} ${request.getStackName()}"
    }
  }
}

