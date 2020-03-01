def call(String script_path, boolean stdout = false, String params = "") {
    return sh(returnStdout: stdout, script: "chmod +x ${script_path} && ${script_path} ${params}")
}
