import com.duvalhub.appconfig.AppConfig
import org.codehaus.groovy.runtime.metaclass.MissingPropertyExceptionNoStack
import org.codehaus.groovy.runtime.typehandling.GroovyCastException

def call() {
    AppConfig conf = valueOf(readYaml(file:'config.yml'))
    echo conf.toString()
    return conf
}

AppConfig valueOf(Map<String, Object> source) {
    echo source.toString()
    try {
        return new AppConfig(source)
    } catch (MissingPropertyException | GroovyCastException  e) {
        echo e.getMessage()
        echo e
        source.remove(e.property)
        return valueOf(source)
    }
}