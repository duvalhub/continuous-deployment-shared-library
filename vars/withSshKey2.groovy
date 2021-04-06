def call(String host, String credentialId, Closure body) {
    echo "### Setting SSH Config File for $host using $credentialId..."
    String keyFileVar = "SSH_KEY_PATH"
    withCredentials([
            sshUserPrivateKey(keyFileVariable: keyFileVar, credentialsId: "SERVICE_ACCOUNT_SSH_2")
    ]) {
        String sshFolder = "/home/jenkins/.ssh"
        String sshConfig = "${sshFolder}/config"
        withEnv([
                "SSH_HOME=${sshFolder}",
                "SSH_CONFIG=${sshConfig}",
                "HOST=${host}",
                "CRED_VAR=${keyFileVar}"
        ]) {
            sh 'mkdir -p $SSH_HOME'
            sh 'echo "Host $HOST" > $SSH_CONFIG'
            sh 'echo "HostName vps287088.duvalhub.com" >> $SSH_CONFIG'
            sh 'echo "IdentityFile ${!CRED_VAR}" >> $SSH_CONFIG'
//            sh 'echo "StrictHostKeyChecking=no" >> $SSH_CONFIG'
        }
        sh "cat ${sshConfig}"
        sh "ssh ${host} \"ls -l\""
        sh "exit 1"
        body()
    }
}