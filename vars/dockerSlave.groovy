import com.duvalhub.initializeworkdir.SharedLibrary

def call(Closure body) {
    if(!SharedLibrary.getVersion(env)) {
        initializeSharedLibrary.findVersion()
    }
    String image = 'duvalhub/jenkins-slave:1.0.5.rc1'
    dockerNode([image: image, args: '-v /var/jenkins:/home/jenkins']) {
      body()
    }
}

