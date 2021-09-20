#!/usr/bin/env groovy

def call(String unityVersion) {
  String unityPath = "/Applications/Unity/Hub/Editor/${unityVersion}/Unity.app/Contents/MacOS/Unity"

  lock('unity') {
    sh """
    ${unityPath}
    -runTests
    -batchmode
    -buildTarget iOS
    -testPlatform EditMode
    -testResults Reports/EditModeTests.xml
    -forgetProjectPath
    -logFile log.txt
    -projectPath .
    """
//    sh "${unityPath} -runTests -batchmode -buildTarget iOS -testPlatform EditMode -testResults Reports/EditModeTests.xml"
//    sh "${unityPath} -runTests -batchmode -buildTarget iOS -testPlatform EditMode -testResults Reports/EditModeTests.xml"
//    sh "${unityPath} -runTests -batchmode -buildTarget iOS -testPlatform PlayMode -testResults Reports/PlayModeTests.xml"
  }
}
