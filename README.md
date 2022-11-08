# Jenkins Shared Library for Unity

<a href="https://github.com/artstorm/jenkins-shared-library-unity/actions">
    <img src="https://github.com/artstorm/jenkins-shared-library-unity/actions/workflows/tests.yml/badge.svg" alt="Test Suite" />    
</a>
<a href="https://twitter.com/artstorm">
    <img src="https://img.shields.io/badge/twitter-@artstorm-blue.svg?logo=twitter&logoColor=ffffff&labelColor=383f47" alt="Twitter: @artstorm" />    
</a>
<a href="https://discord.gg/WJn7w5WaU9">
    <img src="https://img.shields.io/badge/chat-discord-blue?logo=discord&logoColor=ffffff&labelColor=383f47" alt="Discord: Bitbebop" />    
</a>

A Jenkins shared library with a collection of functions useful when setting up a pipeline for Unity projects. In addition to Unity specific functionality, there is also a selection of more generic functions that have proven useful when setting up continuous integration for game dev projects.

## Installation
Read the [Extending with Shared Libraries > Using Libraries](https://www.jenkins.io/doc/book/pipeline/shared-libraries/#using-libraries) section in Jenkins User Handbook for full instruction how to add a shared library to a Jenkins instance and how to access the functionality from a `Jenkinsfile`.

Several functions in this library uses functions from the Jenkins plugin [Pipeline Utility Steps](https://plugins.jenkins.io/pipeline-utility-steps/). Make sure this plugin is intalled in the Jenkins instance using this shared library.

## Functions
An overview of the functionality this shared library exposes in pipelines.

### Unity
#### Unity Build Number
This function parses Unity's Project Settings and returns the build number that has been set 
for the Standalone build in Unity's `Project Settings > Player > Build`.

```groovy
steps {
    echo "Unity build number: ${unityBuildNumber()}"
}
```
