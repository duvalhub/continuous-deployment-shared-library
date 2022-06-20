import com.duvalhub.deploy.DeployRequest
import com.duvalhub.initializeworkdir.SharedLibrary

def call(DeployRequest request) {
    stage('Deploy') {
        echo "### Deploying an artifact. DeployRequest: '${request.toString()}'"
        setDockerEnvironment.withCredentials(request.getDockerHost(request.environment), request.getCredentialId()) {
            String composeFilePath = writeCompose(request)
            String prepare_script = "${SharedLibrary.getWorkdir(env)}/libs/deploy/scripts/network/createIfNotExist.sh"
            String params = "--network ${request.getInternalNetwork()}"
            executeScript(prepare_script, false, params)
            String contextString = env.DOCKER_CONTEXT_ID ? "--context ${env.DOCKER_CONTEXT_ID}" : ""
            sh "docker ${contextString} stack deploy --with-registry-auth -c ${composeFilePath} ${request.getStackName()}"
        }
    }
}
