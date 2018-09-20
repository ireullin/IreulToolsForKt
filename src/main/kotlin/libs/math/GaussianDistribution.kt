package libs.math


/**
 * 計算在高斯分佈下該值出現的機率
 * 未完成
 */
class GaussianDistribution(val mean:Double,val stdev:Double,val area:Double=1.0) {
    constructor(s:Statistics):this(s.mean ,s.stdev)

    private val constant1 = area / (stdev * Math.sqrt(2 * Math.PI))
    private val constant2 = 2 * Math.pow(stdev,2.0)

    fun probability(value:Double):Double{
        val tmp = Math.pow(value-mean, 2.0) / constant2
        return constant1 * Math.exp(-tmp)
    }

}