import com.duvalhub.deploy.DeployRequest
import com.duvalhub.writecompose.WriteComposeRequest

def call(DeployRequest request) {
  stage('Deploy') {
    echo "### Deploying an artifact. DeployRequest: '${request.toString()}'"
    WriteComposeRequest writeComposeRequest = new WriteComposeRequest(request)
    String composeFilePath = writeCompose(request)
    setDockerEnvironment.withCredentials(request.getDockerHost(request.environment), request.getCredentialId()) {
      String prepare_script = "${env.PIPELINE_WORKDIR}/deploy/scripts/network/createIfNotExist.sh"
      String params = "--network ${request.getInternalNetwork()}"
      executeScript(prepare_script, false, params)
      sh "docker stack deploy --with-registry-auth -c ${composeFilePath} ${request.getStackName()}"
    }
  }
}

