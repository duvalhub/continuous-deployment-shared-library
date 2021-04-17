import com.duvalhub.appconfig.Database
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
    // Application Name, Application Profiles, Config Label
    environment_variables += "APPLICATION_NAME=${request.getAppName()}" + '\n'
    environment_variables += "APPLICATION_PROFILES=${request.getEnvironment()}" + '\n'
    environment_variables += "CONFIG_LABEL=master" + '\n'

    for (String env : request.getEnvironmentVariables()) {
        environment_variables += env + '\n'
    }

    if (environment_variables) {
        envs.add("ENV_VARIABLES=${environment_variables}")
    }

    // Secrets
    String secrets = ""
    for (String environment_file_id : request.getEnvironmentFileId()) {
        withCredentials([string(credentialsId: environment_file_id, variable: 'FILE')]) {
            String secret_value = sh(returnStdout: true, script: 'echo $FILE').trim()
            secrets += secret_value + '\n'
        }
    }

    // Database
    Database database = request.getDatabase()
    if (database && database.isEnabled()) {
        withCredentials([string(credentialsId: database.getSecretId(), variable: 'FILE')]) {
            String secret_value = sh(returnStdout: true, script: 'echo $FILE').trim()
            secrets += secret_value + '\n'
            envs.add("DATABASE_IMAGE=${database.getImage()}")
            envs.add("DATABASE_IMAGE=${database.getVersion()}")
        }
    }

    if (secrets) {
        envs.add("SECRETS=${secrets}")
    }

    withEnv(envs) {
        def script = "${SharedLibrary.getWorkdir(env)}/${request.scriptPath}"
        def compose = request.compose
        executeScript(script, false, compose)
        return compose

    }
}

