package libs.math

//fun logN(n:Double, v:Double) = Math.log(v) / Math.log(n)

fun logN(n:Number) = {v:Number -> Math.log(v.toDouble()) / Math.log(n.toDouble())}

fun log2(v:Number) = logN(2)(v)
