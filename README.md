# Jenkins Shared Library for Unity

[![Test Suite](https://github.com/artstorm/jenkins-shared-library-unity/actions/workflows/tests.yml/badge.svg)](https://github.com/artstorm/jenkins-shared-library-unity/actions)
[![Mastodon: @johansteen](https://img.shields.io/badge/mastodon-@johansteen-blue.svg?logo=mastodon&logoColor=ffffff&labelColor=383f47)](https://mastodon.gamedev.place/@johansteen)
[![Twitter: @artstorm](https://img.shields.io/badge/twitter-@artstorm-blue.svg?logo=twitter&logoColor=ffffff&labelColor=383f47)](https://twitter.com/artstorm)
[![Discord: Bitbebop](https://img.shields.io/badge/chat-discord-blue?logo=discord&logoColor=ffffff&labelColor=383f47)](https://discord.gg/WJn7w5WaU9)

A Jenkins shared library with a collection of pipeline steps and functionality useful when setting up a Jenkins CI pipeline for Unity projects. In addition to Unity specific functionality, there is also a selection of more generic functions and steps that have proven useful when setting up continuous integration for game dev projects.

## Available Functions

### Unity Specific

| Function                                                                       | Summary                                                           |
| ------------------------------------------------------------------------------ | ----------------------------------------------------------------- |
| [unityBuildNumber](#unity-build-number)                                        | Get the build number set for the Standalone build in Unity.       |
| [unityCodeCoverageReport](#unity-code-coverage-report)                         | Runs Unity's Code Coverage reporter.                              |
| [unityCodeCoverageReportSummary](#unity-code-coverage-report-summary)          | Parses Summary.xml from the Unity generated code coverage report. |
| [unityPublishCodeCoverageHTMLReport](#unity-publish-code-coverage-html-report) | Publishes the Unity Code Coverage Report to the job page.         |
| [unityTestRunner](#unity-test-runner)                                          | Uses Unity's Test Runner to execute the test suite.               |
| [unityTestRunnerReport](#unity-test-runner-report)                             | Create a report from NUnit tests.                                 |
| [tests.summary](#tests)                                                        | Get Junit test result summary.                                    |

### Generic

| Function                                                  | Summary                                                                               |
| --------------------------------------------------------- | ------------------------------------------------------------------------------------- |
| [getBuildType](#get-build-type)                           | Retrieves the build type for the current running build.                               |
| [setJobDisplayName](#set-job-display-name)                | Sets the display name based on the build type.                                        |
| [git.branchName](#git-branch-name)                        | Get the current branch name.                                                          |
| [git.commitSha](#git-commit-sha)                          | Get the full git commit sha for current commit.                                       |
| [git.commitShaShort](#git-commit-sha-short)               | Get the short 7 character git commit sha for current commit.                          |
| [git.getTrailerValue](#git-trailer-value)                 | Get the trailer value for the specified token.                                        |
| [github.pullRequestComment](#github-pull-request-comment) | Creates or updates the Jenkins bot issue comment for the pull request.                |
| [github.ownerRepo](#github-owner-repo)                    | Get the `owner/repo` part from the project's git url.                                 |
| [github.pullRequest](#github-pull-request)                | Returns the pull request the current commit belongs to.                               |
| [github.issueComments](#github-issue-comments)            | Retrieves all comments for an issue/pull request.                                     |
| [github.createIssueComment](#github-create-issue-comment) | Create issue comment.                                                                 |
| [github.updateIssueComment](#github-update-issue-comment) | Update issue comment.                                                                 |
| [github.createCheckRun](#github-create-check-run)         | Create a check run.                                                                   |
| [github.updateCheckRun](#github-update-check-run)         | Update a check run.                                                                   |
| [influxdb.addMeasurement](#influxfb-add-measurement)      | Add a measurement for writing to InfluxDB.                                            |
| [influxdb.write](#influxfb-write-measurements)            | Write all added measurements to InfluxDB.                                             |
| [timers.start](#timers-start)                             | Creates and starts a new timer with the specified name.                               |
| [timers.stop](#timers-stop)                               | Stops the timer with the specified name.                                              |
| [timers.getDuration](#timers-get-duration)                | Gets the duration in ms between start and stop for the timer with the specified name. |
| [utils.getDirectorySize](#utils-get-directory-size)       | Get the size of the provided directory in bytes.                                      |
| [utils.getFileSize](#utils-get-file-size)                 | Get the size of the provided file in bytes.                                           |

## Installation

Read the [Extending with Shared Libraries > Using Libraries](https://www.jenkins.io/doc/book/pipeline/shared-libraries/#using-libraries) section in Jenkins User Handbook for full instruction how to add a shared library to a Jenkins instance and how to access the functionality from a `Jenkinsfile`.

When using the library in a Jenkinsfile, use a version specifier, to avoid surprises if future updates of the library introduces breaking changes.

```groovy
@Library('shared-library@v1.0.4') _
```

### Pipeline Utility Steps

Several functions in this library uses functions from the Jenkins plugin [Pipeline Utility Steps](https://plugins.jenkins.io/pipeline-utility-steps/). Make sure this plugin is installed in the Jenkins instance using this shared library.

### GitHub App

For the GitHub specific steps a GitHub app must be registered to obtain the access token for Jenkins to communicate with the GitHub API. It also requires the Jenkins plugins

## Pipeline Steps and Functions

An overview of the pipeline steps and functionality this shared library exposes.

### Unity Specific

#### Unity Build Number

This function parses Unity's Project Settings and returns the build number that has been set
for the Standalone build in Unity's `Project Settings > Player > Build`.

```groovy
steps {
    echo "Unity build number: ${unityBuildNumber()}"
}
```

#### Unity Code Coverage Report

Runs Unity's Code Coverage reporter. This assumes the Unity project has the Code Coverage package installed.

The reporter takes two parameters, `unityCodeCoverageReport(assemblyFilters, pathFilters)`.

```groovy
post {
    always {
        // Generate Reports from Unit Tests and Code Coverage.
        unityCodeCoverageReport('+Bitbebop.Blitloop,+Dagobah.*,+Cosmos.*', '-**/Assets/Scripts/Input/GameInput.cs')
    }
}
```

#### Unity Code Coverage Report Summary

Parses `Summary.xml` from the Unity generated code coverage report and returns the summary as an object with the summary items as properties. This can then be posted to Discord, Slack, a time series database, or any other destination.

Available properties:

-   coveredLines
-   uncoveredLines
-   coverableLines
-   totalLines
-   lineCoverage

```groovy
def codeCoverage = unityCodeCoverageReportSummary()
echo  "${codeCoverage.lineCoverage}"
```

#### Unity Publish Code Coverage HTML Report

Publishes the Unity Code Coverage Report so it's available from the job page.

```groovy
post {
    always {
        unityPublishCodeCoverageHTMLReport()
    }
}
```

#### Unity Test Runner

Uses Unity's Test Runner to execute the test suite.

```groovy
steps {
    unityTestRunner("EditMode", "iOS", true, env.CODE_COVERAGE_ASSEMBLY_FILTER, env.CODE_COVERAGE_PATH_FILTERS)
}
```

#### Unity Test Runner Report

Creates a merged report from Unitys PlayMode and EditMode test files.

```groovy
post {
    always {
        unityTestRunnerReport()
    }
}
```

#### tests

Parses the results from the Unity test runner and returns the summary as an object. This can then be posted to Discord, Slack, a time series database, or any other destination.

Available properties:

-   total
-   failed
-   skipped
-   passed

```groovy
def testSummary = tests.summary()
echo  "${testSummary.passed}"
```

### Common

#### Get Build Type

Retrieves the build type for the current running build based on branch name prefixes. This is useful to rely on branch names to determine the build logic.

-   internal: `/feature/*`
-   testflight: `/testflight/*`
-   release: `/release/*`
-   standard: `*`

```groovy
steps {
    echo "Build type: ${getBuildType()}"
}
```

#### Set Job Display Name

Sets name for the running job based on git branch name.

```groovy
steps {
    setJobDisplayName()
}
```

#### Git Branch Name

Get the current branch name.

```groovy
steps {
    echo "Git branch name: ${git.branchName()}"
}
```

#### Git Commit SHA

Get the full git commit sha for current commit.

```groovy
steps {
    echo "Git commit SHA: ${git.commitSha()}"
}
```

#### Git Commit SHA Short

Get the short 7 character git commit sha for current commit.

```groovy
steps {
    echo "Git short commit SHA: ${git.commitShaShort()}"
}
```

#### Git Trailer Value

Get the [trailer](https://git-scm.com/docs/git-interpret-trailers) value for the specified token for the current commit.

```groovy
steps {
    echo "Git commit message trailer value : ${git.getTrailerValue('some-token')}"
}
```

#### GitHub Pull Request Comment

Creates or updates the Jenkins bot issue comment for the pull request.

This can be used to have a comment in the PR that Jenkins updates with current information about the build.

```groovy
steps {
    github.pullRequestComment(stringWithComment, 'jenkins[bot]')
}
```

#### GitHub Owner Repo

Get the <owner/repo> part from the project's git url.

```groovy
def owner = github.ownerRepo()
```

#### GitHub Pull Request

Returns the pull request the current commit belongs to.

```groovy
def pr = github.pullRequest()
```

#### GitHub Issue Comments

Retrieves all comments for an issue/pull request.

```groovy
def comment = github.issueComments(42)
```

#### GitHub Create Issue Comment

Create issue comment.

```groovy
github.createIssueComment(42, "some comment")
```

#### GitHub Update Issue Comment

Update issue comment.

```groovy
github.updateIssueComment(42, "some updated comment")
```

#### GitHub Create Check Run

Create a check run.

Start a check run for a specific build build.

```groovy
steps {
    script {
        github.createCheckRun('Build iOS', 'queued')
    }
}
```

#### GitHub Update Check Run

Update a check run.

```groovy
steps {
    script {
        github.updateCheckRun('Build iOS', 'in_progress')
    }
}

post {
failure {
    script {
        github.updateCheckRun('Build iOS', '', 'failure')
    }
}

success {
    script {
        github.updateCheckRun('Build iOS', '', 'success')
    }
}
```

#### InfluxFB Add Measurement

Add a measurement for writing to InfluxDB. The data type for each field in teh measurement is set with a string.

| value | Data type |
| ----- | --------- |
| `"f"` | float     |
| `"i"` | integer   |
| `"s"` | string    |

```groovy
influxdb.addMeasurement("build",
    [
        platform: platform,
        build_type: buildType
    ],
    [
        code_coverage: ["f", coverage.lineCoverage],
        size: ["i", size],
        duration_unity: ["i", timers.getDuration("Unity.${platform}")],
        duration_xcode: ["i", timers.getDuration("Xcode.${platform}")],
        jenkins_build_number: ["i", currentBuild.number],
        unity_build_number: ["i", unityBuildNumber()],
        commit_sha: ["s", env.GIT_COMMIT_SHA_SHORT]
    ]
)
```

#### InfluxFB Write Measurements

Write all added measurements to InfluxDB.

```groovy
withCredentials([string(credentialsId: 'influxdb', variable: 'INFLUXDB_TOKEN')]) {
    influxdb.write(env.INFLUXDB_HOST, env.INFLUXDB_ORG, env.INFLUXDB_BUCKET, INFLUXDB_TOKEN)
}
```

#### Timers Start

Creates and starts a new timer with the specified name.

Multiple timers can be created and started to store the duration for different steps of the pipeline.

```groovy
timers.start("unity.${platform}")
buildUnity(platform)
timers.stop("unity.${platform}")
```

#### Timers Stop

Stops the timer with the specified name.

```groovy
timers.start("unity.${platform}")
buildUnity(platform)
timers.stop("unity.${platform}")
```

#### Timers Get Duration

Gets the duration in ms between start and stop for the timer with the specified name.

```groovy
echo "Unity iOS Build Duration: ${timers.getDuration('unity.iOS')}"
```

#### Utils Get Directory Size

Get the size of the provided directory in bytes.

```groovy
def size = utils.getDirectorySize('build.app')
```

#### Utils Get File Size

Get the size of the provided file in bytes.

```groovy
def size = utils.getFileSize('build.ipa')
```

## Development

Gradle is used to test the pipeline during development.

### Running Tests

The test suite is executed with:

```sh
./gradlew test
```

Then gradle is executed for the first time in a session a daemon is started. Manage the daemon with these commands.

```sh
## See running daemons
gradle --status

## Stop running daemons
gradle --stop
```

### macOS

Gradle needs Java runtime, and the version of Gradle used in this project uses Java 17. Install Java 17 with Homebrew and then set `JAVA_HOME` in the terminal, for the session, in the terminal.

```
export JAVA_HOME=/usr/local/opt/openjdk@17
```

### Debug Output

During development it can be useful to get output from tests to the console.

```groovy
@Test
void foo_SomeBar_GetSomeBaz() {
    println(someObjectToDebug)
}
```

Enable log info level to display `println` statements in the test output.

```sh
./gradlew test --info
```
