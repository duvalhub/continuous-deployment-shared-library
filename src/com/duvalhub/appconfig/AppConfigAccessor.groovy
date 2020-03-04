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

    String getAppName() {
        return this.appConfig.app.name
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
   
    String getBuilder(){
        return this.appConfig.build.builder
    }

    Build getBuild() {
        return this.appConfig.build
    }

    String getImage() {
        return this.request.getDockerImage()
    }
    
    String getDeployPort() {
        return this.appConfig.deploy.port
    }


    String getStackName(String environment){
        return String.format("%s-%s", this.appConfig.app.group, environment)
    }

    String getInternalNetwork(String environment) {
        return String.format("%s_internal", this.getStackName(environment))
    }

    String getDomainNames(String environment) {

        Platform platform = this.getPlatform(environment)
        String urls = ""

        if ( platform.defaultHostname ) {
            String name = this.appName
            String group = this.config.app.group
            String base = this.base
            urls += [this.appName, this.config.app.group, environment, this.base].join(".")
        }

        if(platform.hostname) {
           urls += "," + platform.hostname
        }

        return urls        
    }

    String getVolumes(String environment) {
        Platform platform = this.getPlatform(environment)
        String volumes_string = ""
        for (Volume volume: platform.volumes) {
            volumes_string += "${volume.toString()} ";
        }
        return volumes_string
    }       

    String getEnvironmentFileId(String environment) {
        Platform platform = this.getPlatform(environment)
        String string = ""
        for (String environmentFile: platform.environmentFiles) {
            string += "${environmentFile} ";
        }
        return string        
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

}