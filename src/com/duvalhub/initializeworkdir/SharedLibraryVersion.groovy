package com.duvalhub.initializeworkdir

import org.jenkinsci.plugins.workflow.cps.EnvActionImpl

class SharedLibraryVersion {
    static void set(EnvActionImpl env, String value) {
        env[SharedLibraryEnvironmentVariable.SHARED_LIBRARY_ENVIRONMENT_VARIABLE.name()] = value;
    }
    static String get(EnvActionImpl env) {
        return env[SharedLibraryEnvironmentVariable.SHARED_LIBRARY_ENVIRONMENT_VARIABLE.name()];
    }
}

enum SharedLibraryEnvironmentVariable {

    SHARED_LIBRARY_ENVIRONMENT_VARIABLE("SHARED_LIBRARY_VERSION");

    String name;

    SharedLibraryEnvironmentVariable(String name) {
        this.name = name;
    }
}
