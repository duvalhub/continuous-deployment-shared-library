import com.duvalhub.git.GitCloneRequest
import com.duvalhub.git.GitRepo

def call(GitCloneRequest request) {
    echo "#### GitCloning with GitCloneRequest '${request.toString()}'"
    GitRepo gitRepo = request.gitRepo
    withSshKey() {
        withEnv([
            "GIT_DIRECTORY=${request.directory}",
            "GIT_URL=${request.gitRepo.getUrl()}"
        ]) {
            String script = "${env.PIPELINE_WORKDIR}/scripts/bash/gitclone/gitclone.sh"
            executeScript(script)

            if ( request.gitRepo.branch ) {
                dir( request.directory) {
                    sh "git checkout ${request.gitRepo.branch}"
                }
            }
        }
    }
}

