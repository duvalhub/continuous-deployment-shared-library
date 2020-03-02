package com.duvalhub.appconfig

import com.duvalhub.BaseObject
import com.duvalhub.appconfig.AppConfig
import com.duvalhub.appconfig.Platform
import com.duvalhub.appconfig.DockerHost
import com.duvalhub.appconfig.Build
import com.duvalhub.appconfig.Docker

class AppConfigAccessor extends BaseObject {

    AppConfig appConfig

    AppConfigAccessor(AppConfig appConfig) {
        this.appConfig = appConfig
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
        DockerHost host
        switch(environment) {
            case "build":
                host = this.getBuild().host
            default:
                host = this.getPlatform(environment).host
        }
        return host
    }

    String getDockerUrl(String environment) {
        return this.getDockerHost(environment).getDockerUrl()
    }

    String getBundleId(String environment) {
        return this.getDockerHost(environment).bundleId
    }

    String getCredentialId() {
        return this.getDocker().credentialId
    }

    String getDockerImage(){
        return this.getDocker().getDockerImage()
    }

    Docker getDocker() {
        return this.appConfig.docker
    }

    String getDockerImageFull() {
        return String.format("%s:%s", this.getDockerImage(), this.version)
    }

    String getStackName(){
        return String.format("%s-%s", this.appConfig.app.group, this.environment)
    }

    String getInternalNetwork() {
        return String.format("%s_internal", this.getStackName())
    }

    String getBuilder(){
        return this.appConfig.build.builder
    }

    Build getBuild() {
        return this.appConfig.build
    }

}