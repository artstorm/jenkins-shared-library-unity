#!/usr/bin/env groovy

/**
 * Git functions.
 *
 * Multiple functions in one file:
 * https://stackoverflow.com/q/69119132/1152087
 */

/**
 * Get the current branch name, with origin removed.
 *
 * `env.BRANCH_NAME` is set automatically in a multibranch pipeline,
 * but not in regular pipeline, so we get it ourselves.
 *
 * Source:
 * https://stackoverflow.com/q/39421257/1152087
 */
def branchName() {
  return env.GIT_BRANCH.replaceFirst(/^.*?\//, '')
}

/**
 * Get the full git commit sha for current commit.
 */
 def commitSha() {
  return sh(returnStdout: true, script: "git rev-parse HEAD").trim();
}

/**
 * Get the short 7 character git commit sha for current commit.
 */
def commitShaShort() {
  return sh(returnStdout: true, script: "git rev-parse --short HEAD").trim();
}
