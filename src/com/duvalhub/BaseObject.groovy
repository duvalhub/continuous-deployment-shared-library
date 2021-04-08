package com.duvalhub

import groovy.json.JsonBuilder

trait IgnoreUnknownProperties {
    def propertyMissing(String name, value) {
        // do nothing
//        echo "Missing property $name"
    }
}

class BaseObject implements IgnoreUnknownProperties {
    String toString() {
        return new JsonBuilder(this).toPrettyString()
    }
}
