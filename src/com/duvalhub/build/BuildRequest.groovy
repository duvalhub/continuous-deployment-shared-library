package com.duvalhub.build

import com.duvalhub.appconfig.AppConfig
import com.duvalhub.appconfig.AppConfigAccessor

class BuildRequest extends AppConfigAccessor {
    String version

    BuildRequest(AppConfig appConfig, String version) {
        super(appConfig)
        this.version = version
    }
}
