import com.duvalhub.appconfig.DockerHost

def call(DockerHost dockerHost, Closure body) {
    echo "Setting docker environment using SSH. dockerHost: '${dockerHost.toString()}'"
    String host = dockerHost.getUrl()
    String user = dockerHost.getUser()
    withSshKey(host, "SERVICE_ACCOUNT_SSH_2", "jenkins") {
        sh "docker context create ${host} --description 'Context for ${host}' --docker 'host=ssh://${user}@${host}'"
        sh "docker context use ${host}"
        body()
    }
}

def setupTls(DockerHost dockerHost, Closure body) {
    echo "Setting docker environment. dockerHost: '${dockerHost.toString()}'"
    withCredentials([
            dockerCert(credentialsId: dockerHost.bundleId, variable: 'DOCKER_CERT_PATH')
    ]) {
        String docker_url = dockerHost.getDockerUrl()
        withEnv([
                "DOCKER_TLS_VERIFY=1",
                "DOCKER_HOST=${docker_url}"
        ]) {
            body()
        }
    }
}

def withCredentials(DockerHost dockerHost, String credentialId, Closure body) {
    setDockerEnvironment(dockerHost) {
        echo "Login into Docker Registry. credentialId: '${credentialId}'"
        withCredentials([
                usernamePassword(credentialsId: credentialId, usernameVariable: 'DOCKER_CREDENTIALS_USR', passwordVariable: 'DOCKER_CREDENTIALS_PSW')
        ]) {
            sh 'echo "$DOCKER_CREDENTIALS_PSW" | docker login --username "$DOCKER_CREDENTIALS_USR" --password-stdin'
            body()
        }
    }
}
