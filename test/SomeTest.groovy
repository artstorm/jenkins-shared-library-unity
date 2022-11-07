import org.junit.*
import com.lesfurets.jenkins.unit.BasePipelineTest
import static groovy.test.GroovyAssert.*

class SomeTest extends BasePipelineTest {
    @Test
    void testCall() {
        assertEquals("result:", true, true)
    }
}
