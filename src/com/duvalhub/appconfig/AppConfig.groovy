package com.duvalhub.appconfig

import com.duvalhub.BaseObject

class AppConfig extends BaseObject {
    App app
    Build build
    Deploy deploy
    Docker docker
}
class App extends BaseObject {
    String name
    String group
    String version_control
}
class Build extends BaseObject {
    String builder
    String builder_version = "latest"
    String destination
    String container
    String container_version = "alpine"
    String enable_extras = "true"
    DockerHost host
}
class Deploy extends BaseObject {
    String port = "80"
    Platforms platforms
}
class Platforms extends BaseObject {
    Platform base
    Platform dev
    Platform stage
    Platform prod
}
class Platform extends BaseObject {
    String[] hostnames
    Boolean defaultHostname = true
    String baseDomainName
    String[] environments
    String[] environmentFiles
    Volume[] volumes
    Network[] networks
    DockerHost host
    int replicas = 1
}
class Volume extends BaseObject {
    String name
    String destination

    String toString() {
        return String.format("%s:%s", this.name, this.destination)
    }
}
class Network extends BaseObject {
    String name
    boolean external

    String toString() {
        if(external) {
            return String.format("%s;external", this.name)
        }
        return this.name
    }
}
class Docker extends BaseObject {
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
