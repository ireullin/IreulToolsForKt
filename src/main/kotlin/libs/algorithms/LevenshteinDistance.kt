package libs.algorithms

import libs.json.toJson
import java.lang.StringBuilder
import kotlin.math.max


/**
 * 計算ld相似度
 */
fun ldSimilarity(x:String, y:String):Double = LevenshteinDistance(x.toList(), y.toList()).similarity

class LevenshteinDistance <T>(val x:List<T>, val y:List<T>){

    private val matrix = (0..y.size)
            .map {_y->
                (0..x.size).map {_x->
                    when{
                        _y==0 -> _x
                        _x==0 -> _y
                        else -> 0
                    }
                }.toIntArray()
            }
            .toTypedArray()
            .apply {
                (0 until y.size).forEach { _y ->
                    (0 until x.size).forEach { _x ->
                        val edit = if(y[_y]==x[_x]) 0 else 1
                        this[_y+1][_x+1] = min(this[_y][_x]+edit, this[_y+1][_x]+1, this[_y][_x+1]+1)
                    }
                }
            }

    val steps get() = matrix[y.size-1][x.size-1]

    val similarity get() = 1.0 - (steps.toDouble() / max(y.size, x.size))

    private fun min(vararg v:Int):Int{
        var min = Int.MAX_VALUE
        v.forEach {
            if(it<min)
                min = it
        }
        return min
    }

    override fun toString():String{
        val sep = "\t"
        val buff = StringBuilder(50)
        buff.append(sep+sep).append(x.map{it.toString()}.joinToString(sep)).append("\n")
        (0..y.size).forEach { j ->
            if(j>0) {
                buff.append(y[j - 1])
            }

            (0..x.size).forEach { i ->
                buff.append(sep).append(matrix[j][i])
            }
            buff.append("\n")
        }

        return buff.toString()
    }
}
