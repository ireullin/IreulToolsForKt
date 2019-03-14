package libs.math

//fun logN(n:Double, v:Double) = Math.log(v) / Math.log(n)

fun logN(n:Number) = {v:Number -> Math.log(v.toDouble()) / Math.log(n.toDouble())}

fun log2(v:Number) = logN(2)(v)

/**
 * 計算衰減的公式,
 * f(1) = initial,
 * f(initial) = 0,
 * 若要計算百分比則再除以 initial
 * @param initial 要衰減到零需要幾次
 * @param alpha 衰減的曲率
 */
class Decay(private val initial:Double, private val alpha:Double){
    private val initialLog = logN(initial)
    fun on(x:Double) :Double{
        val a = Math.pow(initial, alpha)
        val b = Math.pow(initial * initialLog(x), alpha)
        return Math.pow(a-b, 1/alpha)
    }
}