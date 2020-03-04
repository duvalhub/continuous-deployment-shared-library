package com.duvalhub.writecompose

import com.duvalhub.BaseObject
import com.duvalhub.appconfig.AppConfig
import groovy.json.JsonBuilder
import com.duvalhub.deploy.DeployRequest
import com.duvalhub.appconfig.Platform
import com.duvalhub.appconfig.Volume

class WriteComposeRequest extends BaseObject {
    DeployRequest request
    AppConfig config
    String base = "philippeduval.ca"
    String scriptPath = "deploy/scripts/processYml/processYml.sh"
    String compose = "docker-compose.yml"
    String appName
    String image
    String hosts
    String port

    WriteComposeRequest(DeployRequest request) {
        this.request = request
        this.config = request.appConfig
        this.appName = this.config.app.name
        if (this.config.deploy) {
            if( this.config.deploy.hosts )  {
                this.hosts = this.config.deploy.hosts
            }
            if ( this.config.deploy.port ) {
                this.port = this.config.deploy.port
            }
        }
    }
 
}
