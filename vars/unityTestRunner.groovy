#!/usr/bin/env groovy

/**
 * Uses Unity's Test Runner to execute the test suite and capture code coverage.
 *
 * A `UNITY_PATH` env variable must exist with the absolute path to Unity.
 *
 * @param mode `EditMode` or `PlayMode`.
 * @param platform `iOS`, `tvOS`, or `StandaloneOSX` (Check Unity doc for more).
 *
 * @param useCodeCoverage Determine if a code coverage report should be generated.
 * @param assemblyFilters Filters which assemblies to include/exclude in code coverage report.
 * @param pathFilters Filters which paths that should be included or excluded in the coverage report.
 * @param testFilter Add a filter of which tests to run.
 */
def call(String mode, String platform, Boolean useCodeCoverage = false, String assemblyFilters = '', String pathFilters = '', String testFilter = '') {
  // Setup code coverage parameters to use with the testrunner, if `useCodeCoverage` is enabled.
  // https://docs.unity3d.com/Packages/com.unity.testtools.codecoverage@1.1/manual/CoverageBatchmode.html
  String codeCoverage = ''
  if (useCodeCoverage) {
    codeCoverage = """ \
    -debugCodeOptimization \
    -enableCodeCoverage \
    -coverageResultsPath Reports/CodeCoverage \
    -coverageOptions "generateAdditionalMetrics;assemblyFilters:${assemblyFilters};pathFilters:${pathFilters}"
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
    -testFilter "${testFilter}" \
    -testResults Reports/Tests/${platform}-${mode}.xml \
    -logFile Logs/tests-${platform}-${mode}.log \
    ${codeCoverage}
    """
  }
}
