import com.duvalhub.build.BuildRequest
import com.duvalhub.appconfig.AppConfig

def call(BuildRequest buildRequest) {
  stage('Build') {
    AppConfig conf = buildRequest.appConfig
    String version = buildRequest.version
    String image = conf.getDockerImage()

    env.IMAGE = "${image}:${version}"

    def basePath = "${env.PIPELINE_WORKDIR}"

    env.TEMPLATE_PATH = "${basePath}/build/templates"
    env.DOCKERFILE_PATH = "${env.TEMPLATE_PATH}/Dockerfile"

    def script = "${basePath}/scripts/bash/build/build.sh"

    def appBasePath =  "${env.APP_WORKDIR}"
    dir(appBasePath) {
      setDockerEnvironment.withCredentials(conf.build.host, conf.docker.credentialId) {
        sh "chmod +x ${script} && bash -c \"${script} --templates $TEMPLATE_PATH --builder ${conf.build.builder} --build-destination ${conf.build.destination} --container ${conf.build.container}\""
      }
    }
  }
}

