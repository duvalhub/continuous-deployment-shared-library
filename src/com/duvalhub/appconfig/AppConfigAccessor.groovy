package com.duvalhub.appconfig

import com.duvalhub.BaseObject

class AppConfigAccessor extends BaseObject {
    String scriptPath = "libs/deploy/scripts/writeCompose/writeCompose.sh"
    String compose = "docker-compose.yml"

    AppConfig appConfig

    AppConfigAccessor(AppConfig appConfig) {
        this.appConfig = appConfig
    }

    // App
    App getApp() {
        return this.appConfig.app
    }

    String getAppName() {
        return this.getApp().name
    }

    String getVersionControl() {
        return this.getApp().version_control
    }

    String getStackName(String environment) {
        return String.format("%s-%s", this.getApp().group, environment)
    }

    String getInternalNetwork(String environment) {
        return String.format("%s_internal", this.getStackName(environment))
    }

    // Docker
    Docker getDocker() {
        return this.appConfig.docker
    }

    String getRegistry() {
        return this.getDocker().registry
    }

    String getRegistryApi() {
        return this.getDocker().registryApi
    }

    String getNamespace() {
        return this.getDocker().namespace
    }

    String getRepository() {
        return this.getDocker().repository
    }

    String getCredentialId() {
        return this.getDocker().credentialId
    }

    String getDockerImage() {
        return this.getDocker().getDockerImage()
    }

    String getDockerImageFull(String version) {
        return String.format("%s:%s", this.getDockerImage(), this.version)
    }

    // Build
    Build getBuild() {
        return this.appConfig.build
    }

    String getBuilder() {
        return this.appConfig.build.builder
    }

    String getBuilderVersion() {
        return this.appConfig.build.builder_version
    }

    String getBuilderCommand() {
        return this.appConfig.build.builder_command
    }

    String getBuildDestination() {
        return this.appConfig.build.destination
    }

    String getContainer() {
        return this.appConfig.build.container
    }

    String getContainerVersion() {
        return this.appConfig.build.container_version
    }

    String removeApplicationYml() {
        return this.appConfig.build.remove_application_yml
    }

    // Deploy
    String getDeployPort() {
        return this.appConfig.deploy.port
    }

    Database getDatabase() {
        return this.appConfig.deploy.database
    }

    String getDomainNames(String environment) {
        Platform platform = this.getPlatform(environment)
        String base = platform.getBaseDomainName()
        def urls = []

        if (base && platform.defaultHostname) {
            String name = this.appName
            String group = this.appConfig.app.group
            urls.add([name, group, environment, base].join("."))
        }

        if (platform.hostnames) {
            urls.addAll(platform.hostnames)
        }

        return urls.join(" ")
    }

    HealthCheck getHealthcheck() {
       return this.appConfig.deploy.healthcheck
    }

    String getVolumes(String environment) {
        Platform platform = this.getPlatform(environment)
        String volumes_string = ""
        for (Volume volume : platform.volumes) {
            volumes_string += "${volume.toString()} "
        }
        return volumes_string
    }

    String getNetworks(String environment) {
        Platform platform = this.getPlatform(environment)
        String networks_string = ""
        for (Network network : platform.networks) {
            networks_string += "${network.toString()} "
        }
        networks_string += "${this.getInternalNetwork()};external "
        return networks_string
    }

    String[] getEnvironmentVariables(String environment) {
        Platform platform = this.getPlatform(environment)
        return platform.environments
    }

    String[] getEnvironmentFileId(String environment) {
        Platform platform = this.getPlatform(environment)
        return platform.environmentFiles.values()
    }

    Platform getPlatform(String environment) {
        Platform host
        switch (environment) {
            case "dev":
            case "stage":
            case "prod":
                host = this.appConfig.deploy.platforms[environment] as Platform
                break
            default:
                throw new Exception("Environment can't be mapped: '${environment}'")
        }
        Platform base = this.appConfig.deploy.platforms['base'] as Platform
        Platform merged
        if (base) {
            merged = (base.properties.findAll { k, v -> v }  // p1's non-null properties
                    + host.properties.findAll { k, v -> v }) // plus p2's non-null properties
                    .findAll { k, v -> k != 'class' } as Platform
        } else {
            merged = host
        }
        return merged
    }

    DockerHost getDockerHost(String environment) {
        DockerHost host
        switch (environment) {
            case "build":
                host = this.getBuild().host
                break
            default:
                host = this.getPlatform(environment).host
        }
        return host
    }
}