package libs.algorithms

import junit.framework.TestCase
import org.junit.Test

class CosineSimilarityTest:TestCase() {
    @Test
    fun test1() {
        val a = setOf("we","fucking","never","die")
        val b = setOf("this","soul","can","never","die")
        val c = setOf("go","to","hell")

        assertEquals(CosineSimilarity.of(a,a), 1.0)
        assertEquals(CosineSimilarity.of(a,b), 0.4472135954999579)
        assertEquals(CosineSimilarity.of(c,b), 0.0)

        val cs = CosineSimilarity(a,b)
        println(cs)
        assertEquals(cs.calculate(), 0.4472135954999579)

    }
}