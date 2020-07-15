package com.duvalhub.initializeworkdir;

import java.util.Map;

class SharedLibraryVersion {
    public static void set(Map<String,String> env, String value) {
        env.put(SharedLibraryEnvironmentVariable.SHARED_LIBRARY_ENVIRONMENT_VARIABLE.name(), value);
    }
    public static String get(Map<String,String> env) {
       return env.get(SharedLibraryEnvironmentVariable.SHARED_LIBRARY_ENVIRONMENT_VARIABLE.name());
    }
}