def call(String host, String credentialId, String user, Closure body) {
    echo "### Setting SSH Config File for ${host} using ${credentialId}..."
    String credVar = "SSH_KEY_PATH"
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
                "CRED_VAR=${credVar}"
        ]) {
            sh 'mkdir -p $SSH_HOME'
            sh 'echo "Host $HOST" > $SSH_CONFIG'
            sh 'echo "User $SSH_USER" > $SSH_CONFIG'
            sh 'echo "HostName $HOST" >> $SSH_CONFIG'
            sh 'bash -c \'echo "IdentityFile ${!CRED_VAR}" >> $SSH_CONFIG\''
            sh 'echo "StrictHostKeyChecking=no" >> $SSH_CONFIG'
            sh 'echo "UserKnownHostsFile=/dev/null" >> $SSH_CONFIG'
        }
        body()
    }
}