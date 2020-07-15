package com.duvalhub.initializeworkdir

import org.jenkinsci.plugins.workflow.cps.EnvActionImpl

class SharedLibrary {
    static void setWorkdir(EnvActionImpl env, String value) {
        env[SharedLibraryEnvironmentVariable.SHARED_LIBRARY_WORKDIR.name()] = value;
    }
    static String getWorkdir(EnvActionImpl env) {
        return env[SharedLibraryEnvironmentVariable.SHARED_LIBRARY_WORKDIR.name()];
    }
    static void setVersion(EnvActionImpl env, String value) {
        env[SharedLibraryEnvironmentVariable.SHARED_LIBRARY_VERSION.name()] = value;
    }
    static String getVersion(EnvActionImpl env) {
        return env[SharedLibraryEnvironmentVariable.SHARED_LIBRARY_VERSION.name()];
    }
}

enum SharedLibraryEnvironmentVariable {

    SHARED_LIBRARY_VERSION("SHARED_LIBRARY_VERSION"),
    SHARED_LIBRARY_WORKDIR("SHARED_LIBRARY_WORKDIR");

    String name;

    SharedLibraryEnvironmentVariable(String name) {
        this.name = name;
    }
}
