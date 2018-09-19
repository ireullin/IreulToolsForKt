package libs.math

import junit.framework.TestCase
import org.junit.Test
import kotlin.math.log

class LogTest: TestCase() {

    @Test
    fun testLog() {
        val log8 = logN(8)
        assertEquals(log8(8*8*8), 3.0)
        assertEquals(log8(8), 1.0)
        assertEquals(log8(1), 0.0)

        assertEquals(log2(1024), 10.0)
        assertEquals(log2(2), 1.0)
        assertEquals(log2(1), 0.0)
    }
}