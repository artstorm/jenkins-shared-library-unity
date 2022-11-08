package com.bitbebop

/**
 * Cleanup the YAML for a Unity asset.
 *
 * Unity assets uses the YAML format. Each asset start with 3 lines looking
 * something like this:
 *
 * ```
 * %YAML 1.1
 * %TAG !u! tag:unity3d.com,2011:
 * --- !u!114 &1
 * ```
 *
 * These lines makes the readYaml step from Pipeline Utility Steps throw a
 * parsing error. Under the hood it uses SnakeYAML which also throws the same
 * error.
 *
 * We don't need these lines for the purpose we parse these files, so we simple
 * remove the lines with this cleanup utility class.
 */
class CleanUnityAsset {
    static String clean(String assetYaml) {
        // Split the asset YAML into an array based on newline. Drop the 3 first
        // elements and join back into a string separated by newline.
        return assetYaml
            .split('\n')
            .drop(3)
            .join('\n')
    }
}
