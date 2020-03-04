package com.duvalhub.writecompose

import com.duvalhub.BaseObject
import com.duvalhub.appconfig.AppConfig
import groovy.json.JsonBuilder
import com.duvalhub.deploy.DeployRequest
import com.duvalhub.appconfig.Platform
import com.duvalhub.appconfig.Volume

class WriteComposeRequest extends BaseObject {
    String base = "philippeduval.ca"
    String scriptPath = "deploy/scripts/processYml/processYml.sh"
    String compose = "docker-compose.yml"

    String stackName
    String appName
    String image
    String hosts
    String volumes
    String port
    String environmentFileId

    WriteComposeRequest(DeployRequest request) {
        this.stackName = request.getStackName()
        this.appName = request.getAppName()    
        this.image = request.getImage()  
        this.hosts = request.getDeployHosts()
        this.volumes = request.getVolumes()
        this.port = request.getDeployPort()
        this.environmentFileId = request.getEnvironmentFileId()

    }
 
}
