package libs.math

import kotlin.math.roundToInt

fun Double.round(decimal:Int=2):Double{
    val t = Math.pow(10.0, decimal.toDouble())
    return (this*t).roundToInt() /t
}

fun Float.round(decimal:Int=2):Float{
    val t = Math.pow(10.0, decimal.toDouble()).toFloat()
    return (this*t).roundToInt() /t
}