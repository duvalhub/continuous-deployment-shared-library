package com.duvalhub.appconfig

import com.duvalhub.BaseObject

class AppConfigAccessor extends BaseObject {

    String getCredentialId() {
        return this.appConfig.docker.credentialId
    }
}