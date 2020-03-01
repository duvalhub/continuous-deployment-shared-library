def call(Closure body) {
    def dockerSlaveImage = 'duvalhub/jenkins-slave:1.0.4'
    dockerNode(dockerSlaveImage) { 
      body()
    }
}