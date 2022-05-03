#!/usr/bin/env groovy

/**
 * Retrieves the build type for the current running build based on branch name.
 */
def getBuildType(String forKey) {
  def branchName = env.GIT_BRANCH

  if (branchName.contains('/feature/')) {
    return "Internal"
  }

  if (branchName.contains('/testflight/')) {
    return "TestFlight"
  }

  // If the branch did not have special naming, we assume a Standard build.
  return "Standard"
}
