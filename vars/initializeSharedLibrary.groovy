import com.duvalhub.initializeworkdir.InitializeWorkdirIn

def call(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    echo "### Cloning Jenkins into Workdir..."
    env.PIPELINE_WORKDIR = "$WORKSPACE/${params.pipelineWorkdir}"
    echo "### PIPELINE_WORKDIR variable set to '${env.PIPELINE_WORKDIR}'"
    def pipelineBranch = sh(script: "env | grep 'library.shared-library.version' | cut -d '=' -f 2", returnStdout: true).trim()
    sh "rm -rf $PIPELINE_WORKDIR && git clone ${params.pipelineGitRepo.getHttpUrl()} -b ${pipelineBranch} $PIPELINE_WORKDIR"
}

def stage(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    stage("Initialization Shared Library") {
        call(params)
    }
}
gi