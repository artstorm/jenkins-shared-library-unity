#!/usr/bin/env groovy

/**
 * GitHub functions.
 *
 * Multiple functions in one file:
 * https://stackoverflow.com/q/69119132/1152087
 */

/**
 * Get the <owner/repo> part from the project's git url.
 *
 * Source: https://stackoverflow.com/a/69917658/1152087
 */
def ownerRepo() {
  return env.GIT_URL.replaceFirst(/^.*?(?::\/\/.*?\/|:)(.*).git$/, '$1')
}
