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
    String destination = "build"
    String container
    String container_version = "alpine"
    DockerHost host = new DockerHost("docker.build.philippeduval.ca", "DUVALHUB_BUILD_BUNDLE")
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
    String[] environmentFiles
    Volume[] volumes
    DockerHost host

}
class Volume {
    String name
    String destination

    String toString() {
        return String.format("%s:%s", this.name, this.destination)
    }
}
class Docker {
    String registry_api = "https://index.docker.io/v1"
    String registry = "docker.io"
    String namespace
    String repository
    String credentialId

    String getDockerImage(){
        return String.format("%s/%s/%s", this.registry, this.namespace, this.repository)
    }
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

    DockerHost() {}

    DockerHost(String url, String bundleId) {
        this.url = url
        this.bundleId = bundleId
    }
    String getDockerUrl() {
        return String.format("%s://%s:%s", this.protocole, this.url, this.port)
        
    }
}
