import com.duvalhub.appconfig.AppConfig
import org.codehaus.groovy.runtime.metaclass.MissingPropertyExceptionNoStack

def call() {
    AppConfig conf = valueOf(readYaml(file:'config.yml'))
    echo conf.toString()
    return conf
}

AppConfig valueOf(Map<String, Object> source) {
    echo source
    try {
        return new AppConfig(source)
    } catch (MissingPropertyExceptionNoStack e) {
        echo e.getMessage()
        source.remove(e.property)
        return valueOf(source)
    }
}