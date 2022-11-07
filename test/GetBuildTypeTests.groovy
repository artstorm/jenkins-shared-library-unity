import org.junit.*
import com.lesfurets.jenkins.unit.BasePipelineTest
import static groovy.test.GroovyAssert.*

class GetBuildTypeTests extends BasePipelineTest {
    def getBuildType
    def env

    @Before
    void setUp() {
        super.setUp()

        env = this.binding.getVariable('env')

        getBuildType = loadScript("vars/getBuildType.groovy")
    }

    @Test
    void testCall() {
        env['GIT_BRANCH'] = 'main'
        def result = getBuildType()

        assertEquals "result:", "standard", result
    }
}
