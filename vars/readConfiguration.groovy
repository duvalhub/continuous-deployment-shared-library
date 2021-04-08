import com.duvalhub.appconfig.AppConfig
import org.codehaus.groovy.runtime.metaclass.MissingPropertyExceptionNoStack
import org.codehaus.groovy.runtime.typehandling.GroovyCastException

def call() {
//    def yaml = readYaml(file:'config.yml')
    AppConfig conf = readYaml(file:'config.yml')
    echo conf.toString()
    return conf
}

AppConfig valueOf(Map<String, Object> source) {
    echo source.toString()
//    echo instanceof source
    try {
        return new AppConfig(source)
    } catch (MissingPropertyException | GroovyCastException  e) {
        echo e.getMessage()
        echo e.toString()
//        echo e
        source.remove(e.getCause().property)
        return valueOf(source)
    }
}