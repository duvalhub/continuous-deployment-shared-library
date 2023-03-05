import com.duvalhub.appconfig.Database
import com.duvalhub.appconfig.DatabaseType
import com.duvalhub.appconfig.HealthCheck
import com.duvalhub.deploy.DeployRequest
import com.duvalhub.initializeworkdir.SharedLibrary
import hudson.node_monitors.ResponseTimeMonitor

def call(DeployRequest request) {
    echo "Writing Compose file: DeployRequest: '${request.toString()}'"
    def envs = [
            stackName: "STACK_NAME=${request.stackName}",
            appName  : "APP_NAME=${request.appName}",
            image    : "IMAGE=${request.getDockerImageFull()}",
            hosts    : "HOSTS=${request.domainNames}",
            volumes  : "VOLUMES=${request.volumes}",
            networks : "NETWORKS=${request.networks}",
            replicas : "REPLICAS=${request.replicas}"
    ]
    if (request.deployPort) {
        envs["port"] = "PORT=${request.deployPort}"
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
        envs["envVariables"] = "ENV_VARIABLES=${environment_variables}"
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
        switch (database.type) {
            case DatabaseType.SHARED:
                envs["networks"] = envs["networks"] += " database_${request.environment};external"
                break
            case DatabaseType.PRIVATE:
                withCredentials([string(credentialsId: database.getSecretId(), variable: 'FILE')]) {
                    String secret_value = sh(returnStdout: true, script: 'echo $FILE').trim()
                    envs["databaseImage"] = "DATABASE_IMAGE=${database.getImage()}"
                    envs["databaseVersion"] = "DATABASE_VERSION=${database.getVersion()}"
                    envs["databaseSecret"] = "DATABASE_SECRET=${secret_value}"
                }
                break;
        }
    }

    if (secrets) {
        envs["secrets"] = "SECRETS=${secrets}"
    }

    HealthCheck healthCheck = request.getHealthcheck()
    if (healthCheck?.enabled) {
        switch (request.build.container) {
            case "node":
                envs["healthCheckCommand"] = "HEALTHCHECK_COMMAND=node healthcheck.js ${healthCheck.endpoint ?: ""}"
                break
        }
        healthCheck.interval ? envs["healthcheckInterval"] = "HEALTHCHECK_INTERVAL=${healthCheck.interval}" : ""
        healthCheck.timeout ? envs["healthcheckTimeout"] = "HEALTHCHECK_TIMEOUT=${healthCheck.timeout}" : ""
        healthCheck.startPeriod ? envs["healthCheckStartPerios"] = "HEALTHCHECK_START_PERIOD=${healthCheck.startPeriod}" : ""
        healthCheck.retries ? envs["healthcheckRetries"] = "HEALTHCHECK_RETRIES=${healthCheck.retries}" : ""
    }

    withEnv(envs.collect { e -> e.value }) {
        def script = "${SharedLibrary.getWorkdir(env)}/${request.scriptPath}"
        def compose = request.compose
        executeScript(script, false, compose)
        return compose
    }
}

