package com.duvalhub.deploy

import com.duvalhub.BaseObject
import com.duvalhub.appconfig.AppConfig
import com.duvalhub.appconfig.DockerHost
import com.duvalhub.appconfig.Platform

class DeployRequest extends BaseObject {
    AppConfig appConfig
    String appName
    String image
    String version
    String environment

    DeployRequest(AppConfig appConfig, String version, String environment) {
        this.appConfig = appConfig
        this.appName = appConfig.app.name
        this.version = version
        this.environment = environment
    }
}
