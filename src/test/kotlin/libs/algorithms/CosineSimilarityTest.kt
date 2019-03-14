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

    @Test
    fun test2() {
        val a = mapOf("we" to 0.8 ,"fucking" to 0.9 ,"never" to 0.2, "die" to 0.4)
        val b = mapOf("we" to 0.45 ,"fucking" to 0.45 ,"never" to 0.1, "die" to 0.5)
        val cs = CosineSimilarity(a,b)
        println(cs)
        assertEquals(cs.calculate(), 0.9403368069734908)
    }
}