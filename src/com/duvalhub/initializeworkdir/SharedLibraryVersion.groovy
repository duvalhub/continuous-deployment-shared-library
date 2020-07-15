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