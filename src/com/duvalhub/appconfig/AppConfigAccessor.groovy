package com.duvalhub.appconfig

import com.duvalhub.BaseObject

class AppConfigAccessor extends BaseObject {

    DockerHost getDockerHost() {
        return this.getPlatform().host
    }

    String getInternalNetwork() {
        return "${this.getStackName()}_internal"
    }

    String getDockerUrl() {
        return this.getDockerHost().getDockerUrl()
    }

    String getBundleId() {
        return this.getDockerHost().bundleId
    }

    String getCredentialId() {
        return this.appConfig.docker.credentialId
    }
}