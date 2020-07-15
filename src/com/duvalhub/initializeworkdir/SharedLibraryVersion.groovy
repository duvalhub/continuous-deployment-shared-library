package com.duvalhub.initializeworkdir

class SharedLibraryVersion {
    static void set(Map<String,String> env, String value) {
        env.put(SharedLibraryEnvironmentVariable.SHARED_LIBRARY_ENVIRONMENT_VARIABLE.name(), value);
    }
    static String get(Map<String,String> env) {
        return env.get(SharedLibraryEnvironmentVariable.SHARED_LIBRARY_ENVIRONMENT_VARIABLE.name());
    }
}