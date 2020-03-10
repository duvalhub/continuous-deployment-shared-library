package com.duvalhub.deploy


import com.duvalhub.appconfig.AppConfig
import com.duvalhub.appconfig.AppConfigAccessor
import com.duvalhub.appconfig.DockerHost
import com.duvalhub.appconfig.Platform

class DeployRequest extends AppConfigAccessor {
    String appName
    String image
    String version
    String environment

    DeployRequest(AppConfig appConfig, String version, String environment) {
        super(appConfig)
        this.appName = appConfig.app.name
        this.version = version
        this.environment = environment
    }

    String getDockerImageFull() {
        return this.getDockerImageFull(this.version)
    }

    String getStackName(){
        return this.getStackName(this.environment)
    }

    String getInternalNetwork() {
        return this.getInternalNetwork(this.environment)
    }

    String getDomainNames() {
        return this.getDomainNames(this.environment)
    }

    String getVolumes() {
        return this.getVolumes(this.environment)
    }       

    String getEnvironmentFileId() {
        return this.getEnvironmentFileId(this.environment)
    }

    Platform getPlatform() {
        return this.getPlatform(this.environment)
    }

    DockerHost getDockerHost() {
        return this.getDockerHost(this.environment)
    }

    String getDockerUrl() {
        return this.getDockerUrl(this.environment)
    }

    String getBundleId(String environment) {
        return this.getBundleId(this.environment)
    }    
}
