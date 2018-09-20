package libs.math

import junit.framework.TestCase
import org.junit.Test

class GaussianDistributionTest: TestCase() {

    @Test
    fun test() {
        val a = listOf(1,2,3,4,5,6).toDoubleList()
        val stat = a.statistical()
        println(stat)
        val gd = GaussianDistribution(stat.mean, stat.stdev)
        val sum = a.sumByDouble {
            val prob = gd.probability(it)
            println(prob)
            prob
        }
        println(sum)
    }
}