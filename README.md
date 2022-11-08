# Jenkins Shared Library for Unity

[![Test Suite](https://github.com/artstorm/jenkins-shared-library-unity/actions/workflows/tests.yml/badge.svg)](https://github.com/artstorm/jenkins-shared-library-unity/actions)
[![Twitter: @artstorm](https://img.shields.io/badge/twitter-@artstorm-blue.svg?logo=twitter&logoColor=ffffff&labelColor=383f47)](https://twitter.com/artstorm)
[![Discord: Bitbebop](https://img.shields.io/badge/chat-discord-blue?logo=discord&logoColor=ffffff&labelColor=383f47)](https://discord.gg/WJn7w5WaU9)

A Jenkins shared library with a collection of pipeline steps and functionality useful when setting up a Jenkins CI pipeline for Unity projects. In addition to Unity specific functionality, there is also a selection of more generic functions and steps that have proven useful when setting up continuous integration for game dev projects.

## Installation
Read the [Extending with Shared Libraries > Using Libraries](https://www.jenkins.io/doc/book/pipeline/shared-libraries/#using-libraries) section in Jenkins User Handbook for full instruction how to add a shared library to a Jenkins instance and how to access the functionality from a `Jenkinsfile`.

When using the library in a Jenkinsfile, use a version specifier, to avoid surprises if future updates of the library introduces breaking changes.

```groovy
@Library('shared-library@v1.0.3') _
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

- coveredLines
- uncoveredLines
- coverableLines
- totalLines
- lineCoverage

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

- total
- failed
- skipped
- passed

```groovy
def testSummary = tests.summary()
echo  "${testSummary.passed}"
```

### Common

#### Get Build Type
Retrieves the build type for the current running build based on branch name prefixes. This is useful to rely on branch names to determine the build logic. 

- internal: `/feature/*`
- testflight: `/testflight/*`
- release: `/release/*`
- standard: `*` 

```groovy
steps {
    echo "Build type: ${git.getBuildType()}"
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
github.createIssueComment(42, "some updated comment")
```

#### GitHub Create Check Run
Create a check run.

Start a check run for a specific build build.
```groovy
steps {
    checkRunIosId = github.createCheckRun("build_ios", "queued")
}
```

#### GitHub Update Check Run
Update a check run.

```groovy
post {
failure {
    script {
        github.updateCheckRun(checkRunIosId, '', 'failure')
    }
}

success {
    script {
        github.updateCheckRun(checkRunIosId, '', 'success')
    }
}
```