package com.duvalhub

import com.cloudbees.groovy.cps.NonCPS
import groovy.json.JsonBuilder

class BaseObject {
    String toString() {
        return new JsonBuilder(this).toPrettyString()
    }
    @NonCPS
    def propertyMissing(name, value) {
        // nothing
    }
}
