import com.duvalhub.appconfig.AppConfig
import com.duvalhub.appconfig.DockerHost
import com.duvalhub.build.BuildRequest
import com.duvalhub.initializeworkdir.SharedLibrary

def call(BuildRequest buildRequest) {
    stage('Build') {
        echo "### Building an app. BuildRequest: '${buildRequest.toString()}'"
        String version = buildRequest.version
        String image = buildRequest.getDockerImage()
        String image_name = "${image}:${version}"
        def basePath = "${SharedLibrary.getWorkdir(env)}/libs/build"
        String template_path = "${basePath}/templates"
        String dockerfile_path = "${env.TEMPLATE_PATH}/Dockerfile"
        def script = "${basePath}/scripts/build/build.sh"
        def appBasePath = "${env.APP_WORKDIR}"

        withEnv([
                "IMAGE=${image_name}",
                "TEMPLATE_PATH=${template_path}",
                "DOCKERFILE_PATH=${dockerfile_path}"
        ]) {
            dir(appBasePath) {
                DockerHost buildDockerHost = buildRequest.getDockerHost('build')
                String build_destination = buildRequest.getBuildDestination()
                List<String> params = [
                        "--templates $TEMPLATE_PATH",
                        "--builder ${buildRequest.getBuilder()}",
                        "--builder-version ${buildRequest.getBuilderVersion()}",
                        "--container ${buildRequest.getContainer()}",
                        "--container-version ${buildRequest.getContainerVersion()}"
                ] as String[]
                if (build_destination) {
                    params.add("--builder-destination ${build_destination}")
                }
                setDockerEnvironment.withCredentials(buildDockerHost, buildRequest.getCredentialId()) {
                    executeScript(script, false, params.join(" "))
                }
            }
        }
    }
}
