package libs.math

import junit.framework.TestCase
import org.junit.Test

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

    @Test
    fun testDecay(){
        val dacay1 = Decay(100.0, 0.8)
        val dacay2 = Decay(100.0, 1.0)
        val dacay3 = Decay(100.0, 1.4)
        (0..100).forEach {x->
            println("$x\t${dacay1.on(x.toDouble())}\t${dacay2.on(x.toDouble())}\t${dacay3.on(x.toDouble())}")
        }
    }

}