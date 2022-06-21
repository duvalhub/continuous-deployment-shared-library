import com.duvalhub.initializeworkdir.SharedLibrary

def call(String hostname, String credentialId, String user, Closure body) {
    String sshHost = UUID.randomUUID().toString()
    echo "### Setting SSH Config File for ${sshHost} (${hostname}) using ${credentialId}..."
    String credVar = "SKP_${credentialId}"
    String usernameVar = "SKP_SSH_USER"
    withCredentials([
            sshUserPrivateKey(keyFileVariable: credVar, credentialsId: credentialId, usernameVariable: usernameVar)
    ]) {
        String sshFolder = "/home/jenkins/.ssh"
        String sshConfig = "${sshFolder}/config"
        withEnv([
                "SSH_HOME=${sshFolder}",
                "SSH_CONFIG=${sshConfig}",
                "SSH_USER=${user}",
                "SSH_HOST=${sshHost}",
                "HOSTNAME=${hostname}",
                "KEY_FILE_SSH_VAR_NAME=${credVar}"
        ]) {
            String script = "${SharedLibrary.getWorkdir(env)}/libs/scripts/ssh/createConfigFile.sh"
            executeScript(script)
            try {
                body()
            } finally {
                sh "sed -i '/Host ${sshHost}/,+5 d' '${sshConfig}'"
            }
        }
    }
}