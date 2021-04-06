def call (Closure body) {
    echo "### Setting GIT_SSH_COMMAND environment variable"
    withCredentials([
        sshUserPrivateKey(keyFileVariable: 'SSH_KEY_PATH', credentialsId: "SERVICE_ACCOUNT_SSH_2")
    ]) {
        String ssh_key_path = env.SSH_KEY_PATH
        withEnv([
            "GIT_SSH_COMMAND=ssh -oStrictHostKeyChecking=no -oUserKnownHostsFile=/dev/null -i ${ssh_key_path} -F /dev/null"
        ]) {
            body()
        }
    }
}