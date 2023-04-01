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
        def script = "${basePath}/scripts/build/build.sh"
        def appBasePath = "${env.APP_WORKDIR}"
        withConfigServer(buildRequest.getAppName()) {
            withEnv([
                    "IMAGE=${image_name}"
            ]) {
                dir(appBasePath) {
                    DockerHost buildDockerHost = buildRequest.getDockerHost('build')
                    List<String> params = [
                            "--templates ${template_path}",
                            "--builder ${buildRequest.getBuilder()}",
                            "--builder-template ${buildRequest.getBuilderTemplate()}",
                            "--builder-version ${buildRequest.getBuilderVersion()}",
                            "--container ${buildRequest.getContainer()}",
                            "--container-template ${buildRequest.getContainerTemplate()}",
                            "--container-version ${buildRequest.getContainerVersion()}",
                            "--remove-application-yml ${buildRequest.removeApplicationYml()}"
                    ] as String[]

                    String build_destination = buildRequest.getBuildDestination()
                    if (build_destination) {
                        params.add("--build-destination ${build_destination}")
                    }
                    String build_command = buildRequest.getBuilderCommand()
                    if (build_command) {
                        params.add("--build-command ${build_command}")
                    }
                    setDockerEnvironment.withCredentials(buildDockerHost, buildRequest.getCredentialId()) {
                        executeScript(script, false, params.join(" "))
                    }
                }
            }
        }
    }
}
