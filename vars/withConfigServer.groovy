def call(String applicationName, Closure body) {
    withCredentials([
            usernamePassword(credentialsId: "BUILD_CONFIG_CLIENT_PROD", usernameVariable: 'CONFIG_USERNAME', passwordVariable: 'CONFIG_PASSWORD'),
            string(credentialsId: "BUILD_CONFIG_SERVER_URL", variable: "CONFIG_URL")
    ]) {
        withEnv([
                "APPLICATION_NAME=${applicationName}",
                "APPLICATION_PROFILES=prod,prod_remote",
                "CONFIG_LABEL=master"
        ]) {
            sh "env | grep -E 'APPLICATION|CONFIG'"
            body()
        }
    }

}