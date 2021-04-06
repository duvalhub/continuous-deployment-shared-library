def call(String host, String credentialId, Closure body) {
    echo "### Setting SSH Config File for ${host} using ${credentialId}..."
    String keyFileVar = "SSH_KEY_PATH"
    withCredentials([
            sshUserPrivateKey(keyFileVariable: keyFileVar, credentialsId: credentialId)
    ]) {
        String sshFolder = "/home/jenkins/.ssh"
        String sshConfig = "${sshFolder}/config"
        withEnv([
                "SSH_HOME=${sshFolder}",
                "SSH_CONFIG=${sshConfig}",
                "HOST=${host}",
                "CRED_VAR=${keyFileVar}"
        ]) {
            sh 'echo SSH_HOME=$SSH_HOME, SSH_CONFIG=$SSH_CONFIG, HOST=$HOST, CRED_VAR=$CRED_VAR'
            sh 'mkdir -p $SSH_HOME'
            sh 'echo "Host $HOST" > $SSH_CONFIG'
            sh 'echo "HostName $HOST" >> $SSH_CONFIG'
            sh 'bash -c \'echo "IdentityFile ${!CRED_VAR}" >> $SSH_CONFIG\''
            sh 'echo "StrictHostKeyChecking=no" >> $SSH_CONFIG'
        }
        sh "cat ${sshConfig}"
        sh "ssh ${host} \"ls -l\""
        sh "exit 1"
        body()
    }
}