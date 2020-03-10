import com.duvalhub.appconfig.AppConfig
import com.duvalhub.build.BuildRequest

def call(BuildRequest buildRequest) {
  stage('Build') {
    echo "### Building an app. BuildRequest: '${buildRequest.toString()}'"
    AppConfig conf = buildRequest.appConfig
    String version = buildRequest.version
    String image = buildRequest.getDockerImage()
    String image_name = "${image}:${version}"
    def basePath = "${env.PIPELINE_WORKDIR}/libs/build"
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
        setDockerEnvironment.withCredentials(buildRequest.getDockerHost('build'), buildRequest.getCredentialId()) {
          sh "chmod +x ${script} && bash -c \"${script} --templates $TEMPLATE_PATH --builder ${buildRequest.getBuilder()} --build-destination ${conf.build.destination} --container ${conf.build.container}\""
        }
      }
    }
  }
}

