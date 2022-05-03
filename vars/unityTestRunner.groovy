/**
 * Uses Unity's Test Runner to execute the test suite and capture code coverage.
 *
 * A `UNITY_PATH` env variable must exist with the absolute path to Unity.
 *
 * @param mode `EditMode` or `PlayMode`.
 * @param platform `iOS`, `tvOS`, or `StandaloneOSX` (Check Unity doc for more).
 *
 * @param useCodeCoverage Determine if a code coverage report should be generated.
 * @param assemblyFilter Filter which assemblies to include/exclude in code coverage report.
 */
def call(String mode, String platform, Boolean useCodeCoverage = false, String assemblyFilter = '') {
  // Setup code coverage parameters to use with the testrunner, if `useCodeCoverage` is enabled.
  // https://docs.unity3d.com/Packages/com.unity.testtools.codecoverage@1.1/manual/CoverageBatchmode.html
  String codeCoverage = ''
  if (useCodeCoverage) {
    codeCoverage = """ \
    -debugCodeOptimization \
    -enableCodeCoverage \
    -coverageResultsPath Reports/CodeCoverage \
    -coverageOptions "generateAdditionalMetrics;assemblyFilters:${assemblyFilter}"
    """
  }

  /*
    - forgetProjectPath
        Don't save the current Project into the Unity hub history.
  */
  lock('unity') {
    sh """
    ${env.UNITY_PATH} \
    -runTests \
    -batchmode \
    -projectPath . \
    -forgetProjectPath \
    -buildTarget ${platform} \
    -testPlatform ${mode} \
    -testResults Reports/Tests/${platform}-${mode}.xml \
    -logFile Logs/tests-${platform}-${mode}.log \
    ${codeCoverage}
    """
  }
}
