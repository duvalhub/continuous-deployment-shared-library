import com.duvalhub.appconfig.AppConfig

def call() {
    AppConfig conf = valueOf(readYaml(file:'config.yml'))
    echo conf.toString()
    return conf
}

AppConfig valueOf(Map<String, Object> params) {
    try {
        return new AppConfig(source)
    } catch (MissingPropertyException e) {
//        log.info(e.getMessage())
        source.remove(e.property)
        return valueOf(source)
    }
}