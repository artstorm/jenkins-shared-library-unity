#!/usr/bin/env groovy
/**
 * Collection of utility funcitons.
 */

/**
 * Get the size of the provided file in bytes.
 *
 * Remark:
 * To first get the size, and then return it in a separate step is intentional.
 * Otherwise it will fail silently in the log without an error message if the
 * file does not exist.
 */
int getFileSize(String path) {
    def size = sh(returnStdout: true, script: "wc -c < ${path}").trim()

    return size as int
}

/**
 * Get the size of the provided directory in bytes.
 *
 * Source:
 * https://superuser.com/a/402000
 *
 * Remark:
 * To first get the size, and then return it in a separate step is intentional.
 * Otherwise it will fail silently in the log without an error message if the
 * directory does not exist.
 */
int getDirectorySize(String path) {
    def size = sh(returnStdout: true, script: "find ${path} -type f -exec ls -l {} \\; | awk '{sum += \$5} END {print sum}'").trim()

    return size as int
}
