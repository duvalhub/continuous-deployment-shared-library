package com.duvalhub

import groovy.json.JsonBuilder


class BaseObject  {
    String toString() {
        return new JsonBuilder(this).toPrettyString()
    }
}
