import com.duvalhub.appconfig.AppConfig
import com.duvalhub.git.GitCloneRequest
import com.duvalhub.git.GitRepo
import com.duvalhub.initializeworkdir.InitializeWorkdirIn
import com.duvalhub.initializeworkdir.SharedLibrary


def call(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    echo "### Initializing Work Directory. InitializeWorkdirIn: '${params.toString()}'"
    def pipelineBranch = SharedLibrary.getVersion(env) ?: "master"
    GitRepo appGitRepo = params.getAppGitRepo()

    echo "### Getting Application Configs"
    def configs = getMergedFile(pipelineBranch, appGitRepo)
    def confStr = configs.toString()
    echo confStr
    sh "exit 1"
    AppConfig appConfig = getMergedFile(pipelineBranch, appGitRepo)

    // Download Shared Library
    initializeSharedLibrary(params)

    // Download App Code if required
    if (params.getCloneAppRepo()) {
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

        echo "### Cloning App into Workdir..."
        GitCloneRequest appRequest = new GitCloneRequest(appGitRepo, params.appWorkdir)
        gitClone(appRequest)
        env.APP_WORKDIR = "$WORKSPACE/${params.appWorkdir}"
    }

    return appConfig
}

def stage(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    stage("Initialization") {
        initializeWorkdir(params)
    }
}

def getMergedFile(String branch, GitRepo gitRepo) {
    String configFile = "config.yml"
    branch = getConfigFile(branch, gitRepo, configFile)
    def configs = readYaml(file: configFile)
    while (configs.parent) {
        def parentFile = 'parent.yml'
        def configUrl = getConfigUrl(branch, configs.parent)
        def response = downloadConfigFile(configUrl, parentFile)
        configs.parent = null
        if (response.status == 200) {
            def parent = readYaml(file: parentFile)
            configs = merge(parent, configs)
            configs.parent = parent.parent
        }
    }
    return configs
}

def merge(Map lhs, Map rhs) {
    return rhs.inject(lhs.clone()) { map, entry ->
        if (map[entry.key] instanceof Map && entry.value instanceof Map) {
            map[entry.key] = merge(map[entry.key], entry.value)
        } else if (map[entry.key] instanceof Collection && entry.value instanceof Collection) {
            map[entry.key] += entry.value
        } else {
            map[entry.key] = entry.value
        }
        return map
    }
}

def getConfigFile(String branch, GitRepo gitRepo, String destination) {
    String configUrl = getConfigUrl(branch, gitRepo.getOrg(), gitRepo.getRepo())
    def response = downloadConfigFile(configUrl, destination);
    if (response.status == 404) {
        if (branch != 'master') {
            echo "Config file not found on branch '${branch}'. Trying branch 'master'"
            return getConfigFile('master', gitRepo, destination)
        }

        if (response.status == 404) {
            echo "Config file not found. Fatal error."
            sh "exit 1"
        }
    }
    echo "File downloaded and is supposedly at ${destination}"
    return branch
}

def getConfigUrl(String branch, String path) {
    return String.format("https://raw.githubusercontent.com/duvalhub/continous-deployment-configs/%s/%s", branch, path)
}

def getConfigUrl(String branch, String[] pathToFile) {
    return getConfigUrl(branch, pathToFile.join("/"))
}

def getConfigUrl(String branch, String org, String repo) {
    return getConfigUrl(branch, [org, repo, "config.yml"])
}

def downloadConfigFile(String configUrl, String destination) {
    echo "Downloading the config file from url: '${configUrl}' to ${destination}"
    return httpRequest(
            authentication: 'SERVICE_ACCOUNT_GITHUB_TOKEN',
            url: configUrl,
            outputFile: destination,
            validResponseCodes: "200,404"
    )
}