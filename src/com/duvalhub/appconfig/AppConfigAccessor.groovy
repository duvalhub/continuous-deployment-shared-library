package com.duvalhub.appconfig

import com.duvalhub.BaseObject

class AppConfigAccessor extends BaseObject {

    String getCredentialId() {
        return this.appConfig.docker.credentialId
    }







    Platform getPlatform(String environment) {
        Platform host
        switch(environment) {
            case "dev":
            case "stage":
            case "prod":
                host = this.appConfig.deploy.platforms[environment]
                break
            default:
                throw new Exception("Environment can't be mapped: '${environment}'")
        }
        return host
    }

    DockerHost getDockerHost(String environment) {
        return this.getPlatform(environment).host
    }
    

    String getDockerUrl(String environment) {
        return this.getDockerHost(environment).getDockerUrl()
    }

    String getBundleId(String environment) {
        return this.getDockerHost(environment).bundleId
    }

    String getCredentialId() {
        return this.appConfig.docker.credentialId
    }

    String getDockerImage(){
        return "${this.appConfig.docker.registry}/${this.appConfig.docker.namespace}/${this.appConfig.docker.repository}"
    }    

    String getDockerImageFull() {
        return "${appConfig.getDockerImage()}:${this.version}"
    }

    String getStackName(){
        return "${this.appConfig.app.group}-${this.environment}"
    }
    String getInternalNetwork() {
        return "${this.getStackName()}_internal"
    }

}