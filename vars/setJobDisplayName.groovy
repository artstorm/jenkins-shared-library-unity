#!/usr/bin/env groovy

/**
 * Sets name for the running job based on git branch name.
 *
 * Currently I've no need to set the description.
 */
def call() {
  switch(getBuildType()) {
    case 'internal':
      currentBuild.displayName = "#${currentBuild.number} - Internal"
      // currentBuild.description = "Internal build."
      break

    case 'testflight':
      currentBuild.displayName = "#${currentBuild.number} - TestFlight"
      // currentBuild.description = "TestFlight build."
      break

    case 'release':
      currentBuild.displayName = "#${currentBuild.number} - Release"
      // currentBuild.description = "Release build."
      break

    default:
      // Standard build, we don't change the name.
      break
  }
}
