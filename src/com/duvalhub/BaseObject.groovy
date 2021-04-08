package com.duvalhub

import groovy.json.JsonBuilder


class BaseObject extends Expando {
    String toString() {
        return new JsonBuilder(this).toPrettyString()
    }

    def propertyMissing(name, value) {
        // nothing
    }
}
