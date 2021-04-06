def call (Closure body) {
    echo "### Setting GIT_SSH_COMMAND environment variable"
    withCredentials([
        sshUserPrivateKey(keyFileVariable: 'SSH_KEY_PATH', credentialsId: "SERVICE_ACCOUNT_SSH_2")
    ]) {
        String ssh_key_path = env.SSH_KEY_PATH
        String sshConfig = "/home/jenkins/.ssh/config"
        sh """
            mkdir -p /home/jenkins/.ssh;
            echo "Host *" > ${sshConfig}";
            echo "HostName vps287088.duvalhub.com" >> ${sshConfig};
            echo "IdentityFile ${env.SSH_KEY_PATH}" >> ${sshConfig};
        """
        sh "cat ${sshConfig}"
        ssh 'ssh root@vps287088.duvalhub.com "ls -l"'
        body()
    }
}