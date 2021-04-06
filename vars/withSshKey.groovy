def call (Closure body) {
    echo "### Setting GIT_SSH_COMMAND environment variable"
    withCredentials([
        sshUserPrivateKey(keyFileVariable: 'SSH_KEY_PATH', credentialsId: "SERVICE_ACCOUNT_SSH")
    ]) {
        String ssh_var_name = "SSH_KEY_PATH"
        withEnv([
            "GIT_SSH_COMMAND=ssh -oStrictHostKeyChecking=no -oUserKnownHostsFile=/dev/null -i ${ssh_var_name} -F /dev/null"
        ]) {
            sh "git config --global user.email \"toto-africa@email.com\""
            sh "git config --global user.name \"Toto Africa\""
            body()
        }
    }
}