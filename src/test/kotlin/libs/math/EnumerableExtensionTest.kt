package libs.math

import junit.framework.TestCase
import org.junit.Test

class EnumerableExtensionTest: TestCase() {

    @Test
    fun testNormalize() {
        val example1 = listOf(1,2,3,4,5,6).toDoubleList()
        val l1Anwser = listOf(
            0.047619047619047616,
            0.09523809523809523,
            0.14285714285714285,
            0.19047619047619047,
            0.23809523809523808,
            0.2857142857142857
        )

        println(example1.normalizeByL1())
        assertEquals(example1.normalizeByL1(), l1Anwser)

        val l2Anwser = listOf(
            0.10482848367219183,
            0.20965696734438366,
            0.3144854510165755,
            0.4193139346887673,
            0.5241424183609592,
            0.628970902033151
        )

        println(example1.normalizeByL2())
        assertEquals(example1.normalizeByL2(), l2Anwser)
    }

    @Test
    fun testMedian() {
        val example1 = (1..100).toList().toDoubleList().shuffled()
        assertEquals(example1.median(), 50.5)

        val example2 = (0..100).toList().toDoubleList().shuffled()
        assertEquals(example2.median(), 50.0)
    }

    @Test
    fun testCovarianceAndPearson() {
        val example1 = listOf(1,2,3,4,5,6).toDoubleList()
        val example2 = listOf(7,8,9,1,2,3).toDoubleList()
        assert(example1.covariance(example2) - 3.8333333333333335 < 0.00000001)
        assert(example1.pearson(example2) - 0.7219295436604962 < 0.00000001)
    }

    @Test
    fun testStatistical() {
        val example1 = listOf(5, 6, 8, 9).toDoubleList()
        val stat = example1.statistical(0)
        val anwser1 = Statistics(sum=28.0, mean=7.0, variance=2.5, stdev=1.5811388300841898, ddof=0)
        assertEquals(stat, anwser1)

        val sumAndMean = example1.sumAndMean()
        val anwser2 = SumAndMean(sum=28.0, mean=7.0)
        assertEquals(sumAndMean, anwser2)
    }

    @Test
    fun testEntropy() {
        val example1 = listOf("a","a","a","a","a","a","a","a")
        assertEquals(example1.entropy(), 0.0)

        val example2 = listOf("0","A","0","0","0.1")
        val example3 = listOf("1","A","0","0","0.5")
        assertEquals(example2.entropy(), 1.3709505944546687)
        assertEquals(example3.entropy(), 1.9219280948873623)
        val ce = example2.concitionalEntropyWith(example3)
        assertEquals(ce, 0.5509775004326937)
    }

}