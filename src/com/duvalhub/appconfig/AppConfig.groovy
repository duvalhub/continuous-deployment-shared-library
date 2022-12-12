package com.duvalhub.appconfig

import com.duvalhub.BaseObject

class AppConfig extends BaseObject {
    App app
    Strategy strategy
    Build build
    Deploy deploy
    Docker docker
}

class Strategy extends BaseObject {
   StrategyType type
}

enum StrategyType {
    MULTI_BRANCH,
    ONE_BRANCH;
}

class App extends BaseObject {
    String name
    String group
    String version_control
}

class Build extends BaseObject {
    String builder
    String builder_version = "latest"
    String builder_command
    String destination
    String container
    String container_version = "alpine"
    String remove_application_yml = "false"
    DockerHost host
}

class Deploy extends BaseObject {
    String port = "80"
    Database database
    Platforms platforms
}

class Database extends BaseObject {
    boolean enabled = true
    String secretId
    String image = "mysql"
    String version = "10.5"
}

enum Environment {
    dev, stage, prod;
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
//    String[] environmentFiles
    Map environmentFiles
    Volume[] volumes
    Network[] networks
    DockerHost host
    int replicas = 1
    Healthcheck healthcheck
}

class Healthcheck extends BaseObject {
    boolean enabled
    String command
    int interval
    int timeout
    int startPeriod
    int retries
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
        if (external) {
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

    String getDockerImage() {
        return String.format("%s/%s/%s", this.registry, this.namespace, this.repository)
    }
}

class DockerHost extends BaseObject {
    String user = "jenkins"
//    String protocol = "tcp"
    String url
//    String port = "2376"
//    String bundleId

//    String getDockerUrl() {
//        return String.format("%s://%s:%s", this.protocol, this.url, this.port)
//    }
}
