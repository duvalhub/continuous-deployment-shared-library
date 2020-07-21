import com.duvalhub.git.GitCloneRequest
import com.duvalhub.git.GitRepo
import com.duvalhub.initializeworkdir.InitializeWorkdirIn
import com.duvalhub.initializeworkdir.SharedLibrary


def call(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    echo "### Initializing Work Directory. InitializeWorkdirIn: '${params.toString()}'"
    def pipelineBranch = SharedLibrary.getVersion(env) ?: "master"

    initializeSharedLibrary(params)

    GitRepo appGitRepo = params.getAppGitRepo()

    if (!appGitRepo) {
        echo "### Lets find 'appGitRepo' from 'checkout scm'"
        dir(params.getAppWorkdir()) {
            checkout scm
            def scmUrl = scm.getUserRemoteConfigs()[0].getUrl()
            def urlParts = scmUrl.split('/')
            String org = urlParts[urlParts.size() - 2]
            String repo = urlParts[urlParts.size() - 1].split('\\.')[0]
            String branch = scm.branches[0].name
            echo "App Git Info: org: '$org', repo: '$repo', branch: '$branch'"
            appGitRepo = new GitRepo(org, repo, branch)
        }
    }
    echo "### Getting app config file"
    getConfigFile(pipelineBranch, appGitRepo)

    echo "### Cloning App into Workdir..."
    GitCloneRequest appRequest = new GitCloneRequest(appGitRepo, params.appWorkdir)
    gitClone(appRequest)
    env.APP_WORKDIR = "$WORKSPACE/${params.appWorkdir}"

}

def stage(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    stage("Initialization") {
        initializeWorkdir(params)
    }
}

def getConfigFile(String branch, GitRepo gitRepo) {
    def response = downloadConfigFile(branch, gitRepo);
    if (response.status == 404) {
        if (branch != 'master') {
            echo "Config file not found on branch '${branch}'. Trying branch 'master'"
            response = downloadConfigFile('master', gitRepo)
        }

        if (response.status == 404) {
            echo "Config file not found. Fatal error."
            sh "exit 1"
        }
    }
    echo "File downloaded and is supposely at config.yml"
    return response
}

def downloadConfigFile(String branch, GitRepo gitRepo) {
    def configUrl = String.format("https://raw.githubusercontent.com/duvalhub/continous-deployment-configs/%s/%s/%s/config.yml", branch, gitRepo.getOrg(), gitRepo.getRepo())
    echo "Downloading the config file from url: '${configUrl}'"

    return httpRequest(
            authentication: 'SERVICE_ACCOUNT_GITHUB_TOKEN',
            url: configUrl,
            outputFile: 'config.yml',
            validResponseCodes: "200,404"
    )
}