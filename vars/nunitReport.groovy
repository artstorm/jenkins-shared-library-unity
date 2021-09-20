#!/usr/bin/env groovy

def call(String unityVersion) {
  // NUnit report
  // https://plugins.jenkins.io/nunit/
  nunit(
    testResultsPattern: 'Reports/*.xml',
    failIfNoResults: true,
    healthScaleFactor: 1.0
  )
}