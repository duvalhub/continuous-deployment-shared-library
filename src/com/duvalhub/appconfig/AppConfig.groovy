package com.duvalhub.appconfig

import com.duvalhub.BaseObject

class AppConfig extends BaseObject {
    App app
    Build build
    Deploy deploy
    Docker docker
}
class App {
    String name
    String group
    String version_control
}
class Build {
    String builder
    String builder_version = "latest"
    String destination
    String container
    String container_version = "alpine"
    DockerHost host
}
class Deploy {
    String[] hostnames
    String port = "80"
    Platforms platforms
}
class Platforms {
    Platform base
    Platform dev
    Platform stage
    Platform prod
}
class Platform {
    String hostname
    Boolean defaultHostname = true
    String[] environments
    String[] environmentFiles
    Volume[] volumes
    Network[] networks
    DockerHost host
}
class Volume {
    String name
    String destination

    String toString() {
        return String.format("%s:%s", this.name, this.destination)
    }
}
class Network {
    String name
    boolean external

    String toString() {
        if(external) {
            return String.format("%s;external", this.name)
        }
        return this.name
    }
}
class Docker {
    String registry = "docker.io"
    String registryApi = "https://registry.hub.docker.com/v1"
    String namespace
    String repository
    String credentialId

    String getDockerImage(){
        return String.format("%s/%s/%s", this.registry, this.namespace, this.repository)
    }
}

class DockerHost extends BaseObject {
    String user = "jenkins"
    String protocol = "tcp"
    String url
    String port = "2376"
    String bundleId

    String getDockerUrl() {
        return String.format("%s://%s:%s", this.protocol, this.url, this.port)
    }
}
