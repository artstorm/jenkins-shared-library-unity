#!/usr/bin/env groovy

int getFileSize(String path) {
    def size = sh(returnStdout: true, script: "wc -c < ${path}").trim()

    return size as int
}

// https://superuser.com/a/402000
int getDirectorySize(String path) {
    def size = sh(returnStdout: true, script: "find ${path} -type f -exec ls -l {} \\; | awk '{sum += \$5} END {print sum}'").trim()

    return size as int
}
