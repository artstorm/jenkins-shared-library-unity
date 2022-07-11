#!/usr/bin/env groovy

/**
 * Retrieves the build type for the current running build based on branch name.
 */
def call() {
  def branchName = env.GIT_BRANCH

  if (branchName.contains('/feature/')) {
    return "internal"
  }

  if (branchName.contains('/testflight/')) {
    return "testflight"
  }

  if (branchName.contains('/release/')) {
    return "release"
  }

  // If the branch did not have special naming, we assume a Standard build.
  return "standard"
}
