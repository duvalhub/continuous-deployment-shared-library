import com.duvalhub.appconfig.AppConfig

def call() {
    AppConfig conf = readYaml file:'config.yml'
    echo conf.toString()
    return conf
}