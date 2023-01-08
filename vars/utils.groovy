#!/usr/bin/env groovy

int getFileSize(String path) {
    return (sh(returnStdout: true, script: "wc -c < ${path}").trim()) as int
}

int getDirectorySize(String path) {
    return (sh(returnStdout: true, script: "find ${path} -type f -exec ls -l {} \\; | awk '{sum += \$5} END {print sum}'").trim()) as int
}
