package com.duvalhub.build

import com.duvalhub.appconfig.AppConfig
import com.duvalhub.appconfig.AppConfigAccessor

class BuildRequest extends AppConfigAccessor {
    AppConfig appConfig
    String version
}
