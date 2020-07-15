import com.duvalhub.appconfig.AppConfig
import com.duvalhub.appconfig.DockerHost
import com.duvalhub.build.BuildRequest

def call(BuildRequest buildRequest) {
  stage('Build') {
    echo "### Building an app. BuildRequest: '${buildRequest.toString()}'"
    AppConfig conf = buildRequest.appConfig
    String version = buildRequest.version
    String image = buildRequest.getDockerImage()
    String image_name = "${image}:${version}"
    def basePath = "${SharedLibrary.getWorkdir(env)}/libs/build"
    String template_path = "${basePath}/templates"
    String dockerfile_path = "${env.TEMPLATE_PATH}/Dockerfile"
    def script = "${basePath}/scripts/build/build.sh"
    def appBasePath =  "${env.APP_WORKDIR}"

    withEnv([
      "IMAGE=${image_name}",
      "TEMPLATE_PATH=${template_path}",
      "DOCKERFILE_PATH=${dockerfile_path}"
    ]) {
      dir(appBasePath) {
        DockerHost buildDockerHost = buildRequest.getDockerHost('build')
        setDockerEnvironment.withCredentials(buildDockerHost, buildRequest.getCredentialId()) {
          executeScript(script, false, "--templates $TEMPLATE_PATH " +
                  "--builder ${buildRequest.getBuilder()} --builder-version ${buildRequest.getBuilderVersion()} " +
                  "--build-destination ${buildRequest.getBuildDestination()} " +
                  "--container ${buildRequest.getContainer()}  --container-version ${buildRequest.getContainerVersion()}")
        }
      }
    }
  }
}

