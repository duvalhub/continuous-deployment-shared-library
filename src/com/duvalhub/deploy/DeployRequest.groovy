package com.duvalhub.deploy

import com.duvalhub.BaseObject
import com.duvalhub.appconfig.AppConfig
import com.duvalhub.appconfig.DockerHost
import com.duvalhub.appconfig.Platform

class DeployRequest extends BaseObject {
    AppConfig appConfig
    String appName
    String image
    String version
    String environment

    DeployRequest(AppConfig appConfig, String version, String environment) {
        this.appConfig = appConfig
        this.appName = appConfig.app.name
        this.version = version
        this.environment = environment
    }

    Platform getPlatform() {
        Platform host
        switch(this.environment) {
            case "dev":
            case "stage":
            case "prod":
                host = this.appConfig.deploy.platforms[this.environment]
                break
            default:
                throw new Exception("Environment can't be mapped: '${this.environment}'")
        }
        return host
    }

    DockerHost getDockerHost() {
        return this.getPlatform().host
    }

    String getInternalNetwork() {
        return "${this.getStackName()}_internal"
    }

    String getDockerUrl() {
        return this.getDockerHost().getDockerUrl()
    }

    String getBundleId() {
        return this.getDockerHost().bundleId
    }

    String getCredentialId() {
        return this.appConfig.docker.credentialId
    }

    String getDockerImage() {
        return "${appConfig.getDockerImage()}:${this.version}"
    }

    String getStackName(){
        return "${this.appConfig.app.group}-${this.environment}"
    }
}
