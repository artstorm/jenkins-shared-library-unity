import org.junit.*
import com.lesfurets.jenkins.unit.BasePipelineTest
import static groovy.test.GroovyAssert.*

import com.bitbebop.CleanUnityAsset

class ReadUnityAssetTests extends BasePipelineTest {
    def assetYaml =
    """%YAML 1.1
    %TAG !u! tag:unity3d.com,2011:
    --- !u!129 &1
    PlayerSettings:
      m_ObjectHideFlags: 0
      serializedVersion: 23
    """

    @Test
    void clean_RawUnityYAML_RemoveFirstThreeLines() {
        def cleanedAssetYaml = CleanUnityAsset.clean(assetYaml);
        def firstLine = cleanedAssetYaml.split('\n')[0]

        assertTrue(firstLine.contains("PlayerSettings"))
    }
}
