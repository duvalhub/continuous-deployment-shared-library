import com.duvalhub.initializeworkdir.SharedLibrary

def call(Closure body) {
    if(!SharedLibrary.getVersion(env)) {
        initializeSharedLibrary.findVersion()
    }

    def dockerSlaveImage = 'duvalhub/jenkins-slave:1.0.4'
    node('docker-ssh') { 
      body()
    }
}