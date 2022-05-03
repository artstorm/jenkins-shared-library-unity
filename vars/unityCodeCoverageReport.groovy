#!/usr/bin/env groovy

/**
 * Notes regarding the Code Coverage report.
 *
 * ## Unity's Reporter
 *
 * - NPath Complexity calculation and Branch Coverage are not implemented at
 *   present so they will always appear as zero in the coverage report.
 *   Source: https://docs.unity3d.com/Packages/com.unity.testtools.codecoverage@1.1/manual/TechnicalDetails.html
 *
 * ## Jenkin's Reporter
 *
 * - I tried using the OpenCover plugin for Jenkins. It works and I get a nice
 *   report inside of Jenkins. Unfortunately is does not merge multiple reports
 *   as I'd expect it to merge. So when having both PlayMode and EditMode reports
 *   the report is not as I expect it to be. And does not match the report
 *   Unity produces.
 *   See more here: https://github.com/jenkinsci/opencover-plugin/issues/8
 *   The recomemendation was to use ReportGenerator to merge the reports first.
 *   That's not a path I want to explore at this time. So for now I skip using
 *   this plugin and only relies on the report I get from Unity.
 *
 *   Unity TestRunner 2.0 might merge Play and Edit mode tests according to
 *   this thread: https://forum.unity.com/threads/unity-test-framework-2-0-ready-for-feedback.1230126/
 *   If that happens, then it might be worth exploring this pluing again.
 *
 *   But as said, for now, I use Unity's reporter and publish the HTML from
 *   the reporter and combine it with other tools like InfluxDB/Grafana.
 */

/**
 * For this to be meaningful, we first need to run Unity TestRunner with
 * `useCodeCoverage` enabled.
 *
 * A `UNITY_PATH` env variable must exist with the absolute path to Unity.
 *
 * @param unityVersion The Unity version to use for generating the report.
 * @param assemblyFilter Filter which assemblies to include/exclude in code coverage report.
 */
def call(String assemblyFilter = '') {
  /*
    - forgetProjectPath
        Don't save the current Project into the Unity hub history.
    - code coverage report
        https://docs.unity3d.com/Packages/com.unity.testtools.codecoverage@1.1/manual/CoverageBatchmode.html
    - coverageResultsPath
        The path `Reports/CodeCoverage` is set in `unityTestRunner` that should
        be run before we generate the report here.
    - coverageHistoryPath
        We save this to the roots of the job folder for the project as we want
        the history to survive throughout the project's life time.
        We get the path to the project's job folder with `JOB_BASE_NAME`.
        `currentBuild.projectName` should most likely work too.
  */
  lock('unity') {
    sh """
    ${env.UNITY_PATH} \
    -batchmode \
    -projectPath . \
    -forgetProjectPath \
    -debugCodeOptimization \
    -enableCodeCoverage \
    -coverageResultsPath Reports/CodeCoverage \
    -coverageHistoryPath "../../jobs/${env.JOB_BASE_NAME}/CodeCoverage History" \
    -coverageOptions "generateHtmlReport;generateHtmlReportHistory;generateBadgeReport;assemblyFilters:${assemblyFilter}" \
    -quit
    """
  }
}
