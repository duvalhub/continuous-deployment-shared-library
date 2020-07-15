import com.duvalhub.git.GitCloneRequest
import com.duvalhub.git.GitRepo

def call(GitCloneRequest request) {
    echo "#### GitCloning with GitCloneRequest '${request.toString()}'"
    GitRepo gitRepo = request.gitRepo
    withSshKey() {
        withEnv([
            "GIT_DIRECTORY=${request.directory}",
            "GIT_URL=${gitRepo.getUrl()}"
        ]) {
            String script = "${SharedLibrary.getWorkdir(env)}/libs/scripts/gitclone/gitclone.sh"
            executeScript(script)

            if ( gitRepo.branch ) {
                dir( request.directory) {
                    sh "git checkout ${gitRepo.branch}"
                }
            }
        }
    }
}

