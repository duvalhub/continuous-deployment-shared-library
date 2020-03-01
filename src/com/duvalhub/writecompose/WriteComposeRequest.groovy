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

    String getStackName() {
        return this.request.getStackName()
    }

    String getImage() {
        return this.request.getDockerImage()
    }

    String getDomainNames() {

        Platform platform = this.request.getPlatform()
        String urls = ""

        if ( platform.defaultHostname ) {
            String name = this.appName
            String group = this.config.app.group
            String env = this.request.environment
            String base = this.base
            urls += [this.appName, this.config.app.group, this.request.environment, this.base].join(".")
        }

        if(platform.hostname) {
           urls += "," + platform.hostname
        }

        return urls        
    }

    String getVolumes() {
        Platform platform = this.request.getPlatform()
        String volumes_string = ""
        for (Volume volume: platform.volumes) {
            volumes_string += "${volume.toString()} ";
        }
        return volumes_string
    }    
}
