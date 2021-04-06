import com.duvalhub.initializeworkdir.SharedLibrary

def call(String host, String credentialId, String user, Closure body) {
    echo "### Setting SSH Config File for ${host} using ${credentialId}..."
    String credVar = "SKP_${credentialId}"
    withCredentials([
            sshUserPrivateKey(keyFileVariable: credVar, credentialsId: credentialId)
    ]) {
        String sshFolder = "/home/jenkins/.ssh"
        String sshConfig = "${sshFolder}/config"
        withEnv([
                "SSH_HOME=${sshFolder}",
                "SSH_CONFIG=${sshConfig}",
                "SSH_USER=${user}",
                "HOST=${host}",
                "KEY_FILE_SSH_VAR_NAME=${credVar}"
        ]) {
            String script = "${SharedLibrary.getWorkdir(env)}/libs/scripts/ssh/createConfigFile.sh"
            executeScript(script)
        }
        body()
    }
}