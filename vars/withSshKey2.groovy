def call (Closure body) {
    echo "### Setting GIT_SSH_COMMAND environment variable"
    String keyFileVar ="SSH_KEY_PATH"
    withCredentials([
        sshUserPrivateKey(keyFileVariable: keyFileVar, credentialsId: "SERVICE_ACCOUNT_SSH_2")
    ]) {
        String ssh_key_path = env.SSH_KEY_PATH
        String sshConfig = "/home/jenkins/.ssh/config"
        sh "mkdir -p /home/jenkins/.ssh"
        sh "echo \"Host *\" > ${sshConfig}"
        sh "echo \"HostName vps287088.duvalhub.com\" >> ${sshConfig}"
        sh "echo \"IdentityFile \$${keyFileVar}\" >> ${sshConfig}"
        sh "echo \"StrictHostKeyChecking=no\" >> ${sshConfig}"


        sh "whoami"

        sh "cat ${sshConfig}"
        sh 'ssh root@vps287088.duvalhub.com "ls -l"'
        body()
    }
}