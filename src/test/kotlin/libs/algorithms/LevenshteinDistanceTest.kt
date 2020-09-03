package libs.algorithms

import junit.framework.TestCase
import org.junit.Test

class LevenshteinDistanceTest :TestCase(){
    @Test
    fun testSimilarity() {
        assertEquals(newLdWithStrings("fuck", "kcuf").similarity, 0.25)
        assertEquals(newLdWithStrings("fuck you", "fuck you").similarity, 1.0)
        assertEquals(newLdWithStrings("fuck you", "fuck you and me").similarity, 0.5333333333333333)
        assertEquals(newLdWithStrings("fuck you", "fuck me").similarity, 0.75)
        assertEquals(newLdWithStrings("水來自海上上海自來水", "上海自來水水來自海上").similarity, 0.30000000000000004)
    }

    @Test
    fun testMatrix(){
        val ld = LevenshteinDistance("水來自海上上海自來水".toList(), "上海自來水水來自海上".toList())
        println(ld)
    }
}