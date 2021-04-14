import com.duvalhub.deploy.DeployRequest
import com.duvalhub.initializeworkdir.SharedLibrary

def call(DeployRequest request) {
    echo "Writing Compose file: DeployRequest: '${request.toString()}'"
    List<String> envs = [
            "STACK_NAME=${request.stackName}",
            "APP_NAME=${request.appName}",
            "IMAGE=${request.getDockerImageFull()}",
            "HOSTS=${request.domainNames}",
            "VOLUMES=${request.volumes}",
            "NETWORKS=${request.networks}",
            "REPLICAS=${request.replicas}"
    ]
    if (request.deployPort) {
        envs.add("PORT=${request.deployPort}")
    }

    String environment_variables = ""
    for (String env : request.getEnvironmentVariables()) {
        environment_variables += env + '\n'
    }
    for (String environment_file_id : request.getEnvironmentFileId()) {
        withCredentials([string(credentialsId: environment_file_id, variable: 'FILE')]) {
            String env_file_content = sh(returnStdout: true, script: 'echo $FILE').trim()
            for(String env: env_file_content.split(" ")) {
                environment_variables += env + '\n'
            }
        }
    }
    if (environment_variables) {
        envs.add("ENV_VARIABLES=${environment_variables}")
    }

    withEnv(envs) {
        def processScript = "${SharedLibrary.getWorkdir(env)}/${request.scriptPath}"
        def compose = request.compose
        executeScript(processScript, false, compose)
        return compose

    }
}

