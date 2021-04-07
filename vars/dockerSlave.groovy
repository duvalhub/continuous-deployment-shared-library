import com.duvalhub.initializeworkdir.SharedLibrary

def call(Closure body) {
    if(!SharedLibrary.getVersion(env)) {
        initializeSharedLibrary.findVersion()
    }
    String image = 'duvalhub/jenkins-slave:1.0.5.rc3'
//    image = "jenkins/agent:alpine"
    dockerNode(image) {
      body()
    }
}

