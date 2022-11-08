import org.junit.*
import com.lesfurets.jenkins.unit.BasePipelineTest
import static groovy.test.GroovyAssert.*

class GetBuildTypeTests extends BasePipelineTest {
    def env
    def getBuildType

    @Before
    void setUp() {
        super.setUp()

        env = this.binding.getVariable('env')
        getBuildType = loadScript("vars/getBuildType.groovy")
    }

    @Test
    void call_GitMainBranch_ReturnsStandardBuild() {
        env['GIT_BRANCH'] = 'main'
        def result = getBuildType()

        assertEquals "result:", "standard", result
    }
}
