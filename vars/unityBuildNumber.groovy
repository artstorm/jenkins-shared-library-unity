import com.bitbebop.CleanUnityAsset

def call() {
    def assetYaml = readFile file: "ProjectSettings/ProjectSettings.asset"

    def cleanedAssetYaml = CleanUnityAsset.clean(assetYaml)
    def asset = readYaml text: cleanedAssetYaml

    return asset.PlayerSettings.buildNumber.Standalone
}
