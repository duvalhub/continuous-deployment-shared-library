import com.duvalhub.initializeworkdir.InitializeWorkdirIn

def call(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    echo "### Cloning Jenkins into Workdir..."
    env.PIPELINE_WORKDIR = "$WORKSPACE/${params.pipelineWorkdir}"
    echo "### PIPELINE_WORKDIR variable set to '${env.PIPELINE_WORKDIR}'"
    def pipelineBranch = env.PIPELINE_BRANCH ?: "master"
    sh "rm -rf $PIPELINE_WORKDIR && git clone '${params.pipelineGitRepo.getHttpUrl()}' -b '${pipelineBranch}' $PIPELINE_WORKDIR"
}

def stage(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    stage("Initialization Shared Library") {
        call(params)
    }
}

def findVersion(){
    node('master') {
        def branch = sh(script: "env | grep 'library.shared-library.version' | cut -d '=' -f 2", returnStdout: true).trim()
        env.PIPELINE_BRANCH = branch
        echo "Setting 'env.PIPELINE_BRANCH' from shared-library version. Found: '$branch'"
        return branch
    }
}