#!/usr/bin/env groovy

/**
 * Creates a merged report from Unitys PlayMode and EditMode test files.
 *
 * Uses the nunit report plugin.
 * https://plugins.jenkins.io/nunit/
 * https://www.jenkins.io/doc/pipeline/steps/nunit/
 */
def call() {
  // NUnit report
  nunit(
    testResultsPattern: 'Reports/Tests/*.xml',
    failIfNoResults: true,
    healthScaleFactor: 1.0
  )
}
