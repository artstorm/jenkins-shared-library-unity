#!/usr/bin/env groovy

/**
 * Git functions.
 *
 * Multiple functions in one file:
 * https://stackoverflow.com/q/69119132/1152087
 */

def gcommitSha = commitSha()
def gcommitShaShort = commitShaShort()

/**
 * Get the full git commit sha for current commit.
 */
 def commitSha() {
  return sh(returnStdout: true, script: "git rev-parse HEAD").trim();
}

/**
 * Get the short 8 character git commit sha for current commit.
 */
def commitShaShort() {
  return sh(returnStdout: true, script: "git rev-parse --short HEAD").trim();
}
