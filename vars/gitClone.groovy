import com.duvalhub.git.GitCloneRequest
import com.duvalhub.git.GitRepo
import com.duvalhub.initializeworkdir.SharedLibrary

def call(GitCloneRequest request) {
    echo "#### GitCloning with GitCloneRequest '${request.toString()}'"
    GitRepo gitRepo = request.gitRepo
    String host = "github.com"
    withSshKey(host, "SERVICE_ACCOUNT_SSH", "git") {
        withEnv([
                "GIT_DIRECTORY=${request.directory}",
                "GIT_URL=${gitRepo.getUrl().replace(host, env.SSH_HOST)}" // Since withSshKey create a random entry named '$SSH_HOST' in ssh config file
        ]) {
            String script = "${SharedLibrary.getWorkdir(env)}/libs/scripts/git/gitclone.sh"
            executeScript(script)
            if (gitRepo.branch) {
                dir(request.directory) {
                    sh "git checkout ${gitRepo.branch}"
                }
            }
        }
    }
}
