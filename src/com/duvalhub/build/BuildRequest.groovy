package com.duvalhub.build

import com.duvalhub.BaseObject
import com.duvalhub.appconfig.AppConfig

class BuildRequest extends BaseObject {
    AppConfig appConfig
    String version
}
