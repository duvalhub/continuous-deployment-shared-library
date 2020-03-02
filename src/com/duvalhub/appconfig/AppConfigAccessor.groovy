package com.duvalhub.appconfig

import com.duvalhub.BaseObject
import com.duvalhub.appconfig.AppConfig
import com.duvalhub.appconfig.Platform
import com.duvalhub.appconfig.DockerHost

class AppConfigAccessor extends BaseObject {

    AppConfig conf

    AppConfigAccessor(AppConfig conf) {
        this.conf = conf
    }

    String getCredentialId() {
        return this.conf.docker.credentialId
    }

    Platform getPlatform(String environment) {
        Platform host
        switch(environment) {
            case "dev":
            case "stage":
            case "prod":
                host = this.conf.deploy.platforms[environment]
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
        return this.conf.docker.credentialId
    }

    String getDockerImage(){
        return this.conf.getDockerImage()
    }    

    String getDockerImageFull() {
        return String.format("%s:%s", this.getDockerImage(), this.version)
    }

    String getStackName(){
        return String.format("%s-%s", this.conf.app.group, this.environment)
    }

    String getInternalNetwork() {
        return String.format("%s_internal", this.getStackName())
    }

    String getBuild(){
        return this.conf.build.builder
    }

}