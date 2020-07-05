import com.duvalhub.deploy.DeployRequest

def call(DeployRequest request) {
    stage('Deploy') {
        echo "### Deploying an artifact. DeployRequest: '${request.toString()}'"
        String composeFilePath = writeCompose(request)
        setDockerEnvironment.withCredentials(request.getDockerHost(request.environment), request.getCredentialId()) {
            String prepare_script = "${env.PIPELINE_WORKDIR}/libs/deploy/scripts/network/createIfNotExist.sh"
            String params = "--network ${request.getInternalNetwork()}"
            executeScript(prepare_script, false, params)
            sh "docker stack deploy --with-registry-auth -c ${composeFilePath} ${request.getStackName()}"
        }
    }
}
