# Jenkins Pipeline Shared Library

- [Shared Libraries in Jenkins Doc](https://www.jenkins.io/doc/book/pipeline/shared-libraries/)



## Functions
### Unity
#### Unity Build Number
This function parses Unity's Project Settings and returns the build number that has been set 
for the Standalone build in Unity's `Project Settings > Player > Build`.

```groovy
steps {
    echo "Unity build number: ${unityBuildNumber()}"
}
```
