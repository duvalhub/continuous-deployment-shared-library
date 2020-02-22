import com.duvalhub.git.GitCloneRequest
import com.duvalhub.initializeworkdir.InitializeWorkdirIn
import com.duvalhub.appconfig.AppConfig

def call(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    echo "### Initializing Work Directory. InitializeWorkdirIn: '${params.toString()}'"
    def pipelineBranch = env.PIPELINE_BRANCH ?: "master"

    initializeSharedLibrary(params)

    String org
    String repo

    echo "### Cloning App into Workdir..."
    if (params.appGitRepo) {
        GitCloneRequest appRequest = new GitCloneRequest(params.appGitRepo, params.appWorkdir)
        gitClone(appRequest)
        org = params.appGitRepo.org
        repo = params.appGitRepo.repo
    } else {
        dir(params.appWorkdir) {
            checkout scm
            def scmUrl = scm.getUserRemoteConfigs()[0].getUrl()
            def urlParts = scmUrl.split('/')
            org = urlParts[urlParts.size() - 2 ]
            repo = urlParts[urlParts.size() - 1].split('\\.')[0]
        }
    }

//    dir(params.appWorkdir) {
        def configUrl = String.format("https://raw.githubusercontent.com/duvalhub/continous-deployment-configs/%s/%s/%s/config.yml", pipelineBranch, org, repo)
        def response = httpRequest(url: configUrl, outputFile: "config.yml")
        if ( response.status == 404 ) {
            echo "Config file not found: '${configUrl}'"
            sh "exit 1"
        }
        env.APP_WORKDIR = "$WORKSPACE/${params.appWorkdir}"
//    }

}

def stage(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    stage("Initialization") {
        initializeWorkdir(params)
    }    
}