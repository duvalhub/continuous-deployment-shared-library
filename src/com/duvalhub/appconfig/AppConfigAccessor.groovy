package com.duvalhub.appconfig

import com.duvalhub.BaseObject

class AppConfigAccessor extends BaseObject {
    String base = "philippeduval.ca"
    String scriptPath = "libs/deploy/scripts/processYml/processYml.sh"
    String compose = "docker-compose.yml"

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

    String getDockerImageFull(String version) {
        return String.format("%s:%s", this.getDockerImage(), this.version)
    }

    Docker getDocker() {
        return this.appConfig.docker
    }

    String getBuilder(){
        return this.appConfig.build.builder
    }

    String getBuilderVersion(){
        return this.appConfig.build.builder_version
    }

    String getBuildDestination() {
        return this.appConfig.build.destination
    }

    String getContainer(){
        return this.appConfig.build.container
    }

    String getContainerVersion(){
        return this.appConfig.build.container_version
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
        def urls = []

        if ( platform.defaultHostname ) {
            String name = this.appName
            String group = this.appConfig.app.group
            String base = this.base
            urls.add([name, group, environment, base].join("."))
        }

        if(platform.hostname) {
           urls.add(platform.hostname)
        }

        return urls.join(",")
    }

    String getVolumes(String environment) {
        Platform platform = this.getPlatform(environment)
        String volumes_string = ""
        for (Volume volume: platform.volumes) {
            volumes_string += "${volume.toString()} "
        }
        return volumes_string
    }
    String getNetworks(String environment) {
        Platform platform = this.getPlatform(environment)
        String networks_string = ""
        for (Network network: platform.networks) {
            networks_string += "${network.toString()} "
        }
        networks_string += "${this.getInternalNetwork()};external "
        return networks_string
    }
    String[] getEnvironmentFileId(String environment) {
        Platform platform = this.getPlatform(environment)
        return platform.environmentFiles
    }

    Platform getPlatform(String environment) {
        Platform host
        switch(environment) {
            case "dev":
            case "stage":
            case "prod":
                host = this.appConfig.deploy.platforms[environment] as Platform
                break
            default:
                throw new Exception("Environment can't be mapped: '${environment}'")
        }
        Platform base = this.appConfig.deploy.platforms['base'] as Platform
        Platform merged = (base.properties.findAll { k, v -> v }  // p1's non-null properties
                + host.properties.findAll { k, v -> v }) // plus p2's non-null properties
                .findAll { k, v -> k != 'class' }      // excluding the 'class' property
        return merged
    }

    DockerHost getDockerHost(String environment) {
        DockerHost host
        switch(environment) {
            case "build":
                host = this.getBuild().host
                break
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