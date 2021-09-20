#!/usr/bin/env groovy

def call(String unityPath) {
  sh "${unityPath} -runTests -batchmode -buildTarget iOS -testPlatform EditMode -testResults Reports/editmodetests.xml"
  sh "${unityPath} -runTests -batchmode -buildTarget iOS -testPlatform PlayMode -testResults Reports/playmodetests.xml"
}