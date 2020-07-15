import com.duvalhub.initializeworkdir.SharedLibraryVersion

def call(Closure body) {
    if(!SharedLibraryVersion.get(env)) {
        initializeSharedLibrary.findVersion()
    }

    def dockerSlaveImage = 'duvalhub/jenkins-slave:1.0.4'
    dockerNode(dockerSlaveImage) { 
      body()
    }
}