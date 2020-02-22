import com.duvalhub.writecompose.WriteComposeRequest
import com.duvalhub.appconfig.AppConfig

def call(WriteComposeRequest request) {
  env.STACK_NAME = request.request.getStackName()
  env.APP_NAME = request.appName
  env.IMAGE = request.getImage()
  env.HOSTS = request.getDomainNames()
  if (request.port) {
    env.PORT = request.port
  }
  def processScript = "${env.PIPELINE_WORKDIR}/${request.scriptPath}"
  def compose = request.compose
  sh "chmod +x ${processScript} && ${processScript} ${compose}"
  return compose
}

