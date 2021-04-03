import com.duvalhub.initializeworkdir.SharedLibrary

def call(Closure body) {
    if(!SharedLibrary.getVersion(env)) {
        initializeSharedLibrary.findVersion()
    }
    dockerNode('duvalhub/jenkins-slave:1.0.5') {
      body()
    }
}

