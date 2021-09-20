#!/usr/bin/env groovy

/**
 * @param unityVersion The unityversion to use for testing
 * @param mode `EditMode` or `PlayMode`
 * @param platform `iOS` or `tvOS`.
 */
def call(String unityVersion, String mode, String platform) {
  String unityPath = "/Applications/Unity/Hub/Editor/${unityVersion}/Unity.app/Contents/MacOS/Unity"

  lock('unity') {
    sh """
    ${unityPath} \
    -runTests \
    -batchmode \
    -buildTarget ${platform} \
    -testPlatform ${mode} \
    -testResults Reports/${platform}-${mode}Tests.xml \
    -forgetProjectPath \
    -logFile Logs/${platform}-${mode}.log \
    -projectPath .
    """
//    sh "${unityPath} -runTests -batchmode -buildTarget iOS -testPlatform EditMode -testResults Reports/EditModeTests.xml"
//    sh "${unityPath} -runTests -batchmode -buildTarget iOS -testPlatform EditMode -testResults Reports/EditModeTests.xml"
//    sh "${unityPath} -runTests -batchmode -buildTarget iOS -testPlatform PlayMode -testResults Reports/PlayModeTests.xml"
  }
}
