import com.duvalhub.initializeworkdir.SharedLibrary

def call(String host, String credentialId, String user, Closure body) {
    echo "### Setting SSH Config File for ${host} using ${credentialId}..."
    String credVar = "SKP_${credentialId}"
    String usernameVar = "SKP_SSH_USER"
    withCredentials([
            sshUserPrivateKey(keyFileVariable: credVar, credentialsId: credentialId, usernameVariable: usernameVar)
    ]) {
        String sshFolder = "/home/jenkins/.ssh"
        String sshConfig = "${sshFolder}/config-${UUID.randomUUID().toString()}"
        withEnv([
                "SSH_HOME=${sshFolder}",
                "SSH_CONFIG=${sshConfig}",
                "SSH_USER=${user}",
                "HOST=${host}",
                "KEY_FILE_SSH_VAR_NAME=${credVar}"
        ]) {
            String script = "${SharedLibrary.getWorkdir(env)}/libs/scripts/ssh/createConfigFile.sh"
            executeScript(script)
            try {
                body()
            } finally {
                sh "rm -rf ${sshConfig}"
            }
        }
    }
}