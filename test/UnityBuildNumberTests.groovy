import org.junit.*
import com.lesfurets.jenkins.unit.BasePipelineTest
import static groovy.test.GroovyAssert.*

class UnityBuildNumberTests extends BasePipelineTest {
    def unityBuildNumber

    @Before
    void setUp() {
        super.setUp()

        helper.registerAllowedMethod('readYaml', [Map]) { args -> return [ PlayerSettings : [ buildNumber: [Standalone : 42]]] }
        unityBuildNumber = loadScript("vars/unityBuildNumber.groovy")
    }

    @Test
    void call_NoArguments_ReturnsStandaloneBuildNumber() {
        def buildNumber = unityBuildNumber()

        assertEquals "result:", 42, buildNumber
    }
}
