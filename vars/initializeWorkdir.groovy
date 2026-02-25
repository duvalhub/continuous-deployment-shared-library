import com.duvalhub.appconfig.AppConfig
import com.duvalhub.git.GitCloneRequest
import com.duvalhub.git.GitRepo
import com.duvalhub.initializeworkdir.InitializeWorkdirIn
import com.duvalhub.initializeworkdir.SharedLibrary


def call(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    echo "### Initializing Work Directory. InitializeWorkdirIn: '${params.toString()}'"
    // Download Shared Library
    initializeSharedLibrary(params)

    def pipelineBranch = params?.configGitBranch ?: SharedLibrary.getVersion(env) ?: "master"

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
    } else if (params.getCloneAppRepo()) {
        echo "### Cloning App into Workdir..."
        GitCloneRequest appRequest = new GitCloneRequest(appGitRepo, params.appWorkdir)
        gitClone(appRequest)
    }
    env.APP_WORKDIR = "$WORKSPACE/${params.appWorkdir}"

    echo "### Getting Application Configs"
    AppConfig appConfig = getMergedFile(params.appConfig, pipelineBranch, appGitRepo)
    echo appConfig.toString()

    return appConfig
}

def stage(InitializeWorkdirIn params = new InitializeWorkdirIn()) {
    stage("Initialization") {
        initializeWorkdir(params)
    }
}

def getMergedFile(Object configs, String branch, GitRepo gitRepo) {
    def previous = []
    if (!configs) {
        String configFile = "config.yml"
        getConfigFile(branch, gitRepo, configFile)
        configs = readYaml(file: configFile)
    }
    while (configs.parent) {
        if (previous.contains(configs.parent)) {
            echo "Infinite loop detected in parent configurations. Exiting..."
            sh "exit 1"
        }
        previous.add(configs.parent)
        def parentFile = 'parent.yml'
        def response = downloadFile("duvalhub", "continuous-deployment-configs", branch, configs.parent, parentFile)
//        String parentUrl = getGitHubRawUrl("duvalhub", "continuous-deployment-configs", branch, configs.parent)
//        var response = downloadConfigFile(parentUrl, parentFile)
//        if(response.status_code)
//            return String.format("https://api.github.com/repos/%s/%s/contents/%s?ref=%s",org, repo, path, branch)
//    return String.format("https://raw.githubusercontent.com/%s/%s/refs/heads/%s/%s", org, repo, branch, path)
//        }
//        String branch, GitRepo gitRepo, String destination
//        def response = getConfigFileFromPipelineConfigs(branch, gitRepo, parentFile)
//        def configUrl = getConfigUrl(branch, configs.parent)
//        def response = downloadConfigFile(configUrl, parentFile)
        configs.parent = null
        if (response.status == 200) {
            def parent = readYaml(file: parentFile)
            configs = merge(parent, configs)
            configs.parent = parent.parent
        }
    }
    AppConfig appConfig = configs as AppConfig
    appConfig.getApp().name = appConfig.getApp().name ? appConfig.getApp().name : gitRepo.repo
    appConfig.getDocker().repository = appConfig.getDocker().repository ? appConfig.getDocker().repository : gitRepo.repo
    return appConfig
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
def getConfigFile(String pipelineBranch, GitRepo appRepo, String destination) {
    def response = getConfigFileFromAppRepo(appRepo, destination)
    if (response.status == 404) {
        return getConfigFileFromPipelineConfigs(pipelineBranch, appRepo, destination)
    }
    echo "File downloaded and is supposedly at ${destination}"
    return response
}

def getConfigFileFromAppRepo(GitRepo gitRepo, String destination) {
    String configUrl = getGitHubRawUrl(gitRepo.getOrg(), gitRepo.getRepo(), env.BRANCH_NAME, ".cicd/config.yml")
//    String configUrl =  String.format("https://raw.githubusercontent.com/%s/%s/refs/heads/%s/.cicd/config.yml", gitRepo.getOrg(), gitRepo.getRepo(), env.BRANCH_NAME)
    return downloadConfigFile(configUrl, destination);
}

def downloadFile(String org, String repo, String branch, String path, String destination) {
    String url = getGitHubRawUrl(org, repo, branch, path)
    def response = downloadConfigFile(url, destination);
    if (response.status == 404) {
        if (branch != 'master') {
            echo "Config file not found on branch '${branch}'. Trying branch 'master'"
            return downloadFile(org, repo, "master", path, destination)
        }

        if (response.status == 404) {
            echo "Config file not found. Fatal error."
            sh "exit 1"
        }
    }
//    echo "File downloaded and is supposedly at ${destination}"
    return response
}

def getConfigFileFromPipelineConfigs(String branch, GitRepo gitRepo, String destination) {
    String configUrl = getConfigUrl(branch, gitRepo.getOrg(), gitRepo.getRepo())
    def response = downloadConfigFile(configUrl, destination);
    if (response.status == 404) {
        if (branch != 'master') {
            echo "Config file not found on branch '${branch}'. Trying branch 'master'"
            return getConfigFileFromPipelineConfigs('master', gitRepo, destination)
        }

        if (response.status == 404) {
            echo "Config file not found. Fatal error."
            sh "exit 1"
        }
    }
//    echo "File downloaded and is supposedly at ${destination}"
    return response
}

def getConfigUrl(String branch, String path) {
    return getGitHubRawUrl("duvalhub", "continuous-deployment-configs", branch, path)
//    return String.format("https://raw.githubusercontent.com/duvalhub/continuous-deployment-configs/%s/%s", branch, path)
}

def getConfigUrl(String branch, String[] pathToFile) {
    return getConfigUrl(branch, pathToFile.join("/"))
}

def getConfigUrl(String branch, String org, String repo) {
    String[] pathToFile = [org, repo, "config.yml"]
    return getConfigUrl(branch, pathToFile)
}

def downloadConfigFile(String configUrl, String destination) {
    echo "Downloading the config file from url: '${configUrl}' to ${destination}"
    withCredentials([
        usernamePassword(credentialsId: "SERVICE_ACCOUNT_GITHUB_TOKEN", usernameVariable: 'USERNAME', passwordVariable: 'TOKEN'),
    ]) {
//       return
        return httpRequest(
//            authentication: 'SERVICE_ACCOUNT_GITHUB_TOKEN',
            url: configUrl,
            customHeaders: [
             [name: 'Authorization', value: "Bearer ${env.TOKEN}", maskValue: true],
             [name: "Accept", value: "application/vnd.github.raw+json"]
            ],
            outputFile: destination,
            validResponseCodes: "200,404"
        )
    }
//    return httpRequest(
//            authentication: 'SERVICE_ACCOUNT_GITHUB_TOKEN',
//            url: configUrl,
//            outputFile: destination,
//            validResponseCodes: "200,404"
//    )
}

static def getGitHubRawUrl(String org, String repo, String branch, String path) {
    return String.format("https://api.github.com/repos/%s/%s/contents/%s?ref=%s",org, repo, path, branch)
//    return String.format("https://raw.githubusercontent.com/%s/%s/refs/heads/%s/%s", org, repo, branch, path)
}