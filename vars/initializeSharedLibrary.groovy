import com.duvalhub.initializeworkdir.InitializeWorkdirIn
import com.duvalhub.initializeworkdir.SharedLibrary

def call(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    echo "### Cloning Jenkins into Workdir..."
    String sharedLibraryWorkdir = "$WORKSPACE/${params.pipelineWorkdir}"
    SharedLibrary.setWorkdir(env, sharedLibraryWorkdir)
    echo "### PIPELINE_WORKDIR variable set to '${SharedLibrary.getWorkdir(env)}'"
    def pipelineBranch = SharedLibrary.getVersion(env) ?: "master"
    sh "rm -rf '${SharedLibrary.getWorkdir(env)}' && git clone '${params.pipelineGitRepo.getHttpUrl()}' -b '${pipelineBranch}' '${SharedLibrary.getWorkdir(env)}'"
}

def stage(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    stage("Initialization Shared Library") {
        call(params)
    }
}

def findVersion(){
    node('master') {
        def branch = sh(script: "env | grep 'library.shared-library.version' | cut -d '=' -f 2", returnStdout: true).trim()
        SharedLibrary.setVersion(env, branch)
        echo "Setting 'env.PIPELINE_BRANCH' from shared-library version. Found: '$branch'"
        return branch
    }
}