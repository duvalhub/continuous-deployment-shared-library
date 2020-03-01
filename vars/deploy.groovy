import com.duvalhub.deploy.DeployRequest
import com.duvalhub.writecompose.WriteComposeRequest
import com.duvalhub.appconfig.DockerHost
import com.duvalhub.appconfig.Platform

def call(DeployRequest request) {
  stage('Deploy') {
    WriteComposeRequest writeComposeRequest = new WriteComposeRequest(request)
    String composeFilePath = writeCompose(writeComposeRequest)
    Platform platform = request.getDockerHost()
    setDockerEnvironment(platform.host) {
      String script = "${env.PIPELINE_WORKDIR}/deploy/scripts/network/createIfNotExist.sh"
      String params = "--network ${request.getInternalNetwork()}"
      executeScript(script, false, params)
      sh "docker stack deploy -c ${composeFilePath} ${request.getStackName()}"
    }
  }
}

