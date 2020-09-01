package libs.algorithms

import junit.framework.TestCase
import org.junit.Test

class LevenshteinDistanceTest :TestCase(){
    @Test
    fun testSimilarity() {
        assertEquals(ldSimilarity("fuck", "kcuf"), 0.25)
        assertEquals(ldSimilarity("fuck you", "fuck you"), 1.0)
        assertEquals(ldSimilarity("fuck you", "fuck you and me"), 0.5333333333333333)
        assertEquals(ldSimilarity("fuck you", "fuck me"), 0.6696428571428572)
        assertEquals(ldSimilarity("水來自海上上海自來水", "上海自來水水來自海上"), 0.75)
    }

    @Test
    fun testMatrix(){
        val ld = LevenshteinDistance("水來自海上上海自來水".toList(), "上海自來水水來自海上".toList())
        println(ld)
    }
}