# Jenkins Shared Library for Unity

<p align="center">
    <a href="https://github.com/artstorm/jenkins-shared-library-unity/actions">
        <img src="https://github.com/artstorm/jenkins-shared-library-unity/actions/workflows/tests.yml/badge.svg" alt="Test Suite" />    
    </a>
    <a href="https://twitter.com/artstorm">
        <img src="https://img.shields.io/badge/twitter-@artstorm-blue.svg?logo=twitter&logoColor=ffffff&labelColor=383f47" alt="Twitter: @artstorm" />    
    </a>
    <a href="https://discord.gg/WJn7w5WaU9">
        <img src="https://img.shields.io/badge/chat-discord-blue?logo=discord&logoColor=ffffff&labelColor=383f47" alt="Discord: Bitbebop" />    
    </a>
</p>

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
