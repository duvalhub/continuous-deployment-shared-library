package com.duvalhub.appconfig

import com.duvalhub.BaseObject

class AppConfig extends BaseObject {
    App app
    Build build
    Deploy deploy
    Docker docker

    String getDockerImage(){
        return "${this.docker.registry}/${this.docker.namespace}/${this.docker.repository}"
    }
}
class App {
    String name
    String group
    String version_control
}
class Build {
    String builder
    String destination = "build"
    String container
    DockerHost host
}
class Deploy {
    String hostnames
    String port = "80"
    Platforms platforms
    DockerHosts hosts
}
class Platforms {
    Platform dev
    Platform stage
    Platform prod
}
class Platform {
    String hostname
    Boolean defaultHostname = true
    Volume[] volumes
    DockerHost host

}
class Volume {
    String name
    String destination
    Boolean external = false

    String toString() {
        if(this.external) {
            return String.format("%s:%s:%s", this.name, this.destination, "external")
        } else {
            return String.format("%s:%s", this.name, this.destination)
        }
    }
}
class Docker {
    String registry_api = "https://index.docker.io/v1"
    String registry = "docker.io"
    String namespace
    String repository
    String credentialId
}

class DockerHosts {
    DockerHost dev
    DockerHost prod
}

class DockerHost extends BaseObject {
    String protocole = "tcp"
    String url
    String port = "2376"
    String bundleId

    String getDockerUrl() {
        return String.format("%s://%s:%s", this.protocole, this.url, this.port)
        
    }
}
