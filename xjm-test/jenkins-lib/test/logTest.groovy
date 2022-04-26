#!groovy

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test

class logTest extends BasePipelineTest {
    def log

    @Before
    void setUp() {
        super.setUp()
        log = loadScript("vars/log.groovy")
    }

    @Test
    void logInfo() {
        log.info("info message")
//        assertThat(helper.methodCallCount("info"), is(1L))
    }

    @Test
    void logWarn() {
        log.warn("warn message")
        def j = helper.methodCallCount("warn")
//        assertThat(j, is(1L))
        printCallStack()
    }
}
