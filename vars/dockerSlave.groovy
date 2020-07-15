def call(Closure body) {

    if(!env.PIPELINE_BRANCH) {
        initializeSharedLibrary.findVersion()
    }

    def dockerSlaveImage = 'duvalhub/jenkins-slave:1.0.4'
    dockerNode(dockerSlaveImage) { 
      body()
    }
}