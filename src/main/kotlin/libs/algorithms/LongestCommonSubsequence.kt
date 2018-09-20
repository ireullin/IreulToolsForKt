package libs.algorithms

import kotlin.math.max

fun newLcsWithStrings(x:String, y:String) = LongestCommonSubsequence(x.toList(), y.toList())

/**
 * 計算lcs相似度
 */
fun lcsSimilarity(x:String, y:String):Double = lcsSimilarity(x.toList(), y.toList())

/**
 * 計算lcs相似度
 */
fun <T> lcsSimilarity(x:List<T>, y:List<T>):Double{
    var score = 0.0
    val lcs = LongestCommonSubsequence(x,y)
    println(lcs)
    val overlappeds = lcs.findOverlappeds().toSet().filter { it.size>1 }
    println(overlappeds)
    overlappeds.forEach{
        val elementSize = it.size.toDouble()
        println("${it} ${elementSize} ")
        score += (elementSize / x.size)
        score += (elementSize / y.size)
    }
    return score / 2
}


class LongestCommonSubsequence<T>(val x:List<T>, val y:List<T>){

    private val matrix:ArrayList<ArrayList<Int>> = arrayListOf()
    private var longest = 0
    private val sep = "\t"

    init{
        (0 until y.size).forEach { j ->
            val line = arrayListOf<Int>()
            (0 until  x.size).forEach { line.add(0) }
            matrix.add(line)
        }

        (0 until y.size).forEach { j ->
            (0 until  x.size).forEach { i ->
                if(y[j]==x[i]){
                    if(j==0 || i==0){
                        matrix[j][i] = 1
                    }else{
                        matrix[j][i] = matrix[j-1][i-1] + 1
                        longest = max(longest, matrix[j][i])
                    }
                }
            }
        }
    }

    val longestSize get() = this.longest


    /**
     * 找出最長的元素
      */
    fun findLongest() = findSizeOf(longestSize)

    /**
     * 找出最長的元素
     */
    inline fun <R> findLongest(join:(List<T>)->R ) = findLongest().map(join)

    /**
     * 找出最長的元素，並轉為字串
     */
    fun findLongestStrings() = findLongest{ it.joinToString("") }

    /**
     * 找出該長度的元素，並轉為字串
     */
    fun findStringSizeOf(size: Int):List<String> = findSizeOf(size){ it.joinToString("") }

    /**
     * 找出該長度的元素
     */
    inline fun <R> findSizeOf(size: Int, join:(List<T>)->R ):List<R> = findSizeOf(size).map(join)

    /**
     * 找出該長度的元素
     */
    fun findSizeOf(size:Int):List<List<T>> {
        val result:ArrayList<List<T>> = arrayListOf()
        (0 until y.size).forEach { j ->
            (0 until x.size).forEach { i ->
                if(matrix[j][i]>=size){
                    result += (0 until matrix[j][i]).map{ y[j-it] }.asReversed()
                }
            }
        }
        return result
    }

    /**
     * 找出所有重疊的元素並轉為字串
     */
    fun findOverlappedStrings():List<String> = findOverlappeds{ it.joinToString("") }

    /**
     * 找出所有重疊的元素
     */
    inline fun <R> findOverlappeds( join:(List<T>)->R ):List<R> = findOverlappeds().map(join)

    /**
     * 找出所有重疊的元素
     */
    fun findOverlappeds():List<List<T>> {
        val result:ArrayList<List<T>> = arrayListOf()
        (0 until y.size).forEach { j ->
            (0 until x.size).forEach { i ->
                if(matrix[j][i]!=0){
                    if((j==y.size-1) || (i==x.size-1) || (matrix[j+1][i+1]==0)){
                        result += (0 until matrix[j][i]).map{ y[j-it] }.asReversed()
                    }
                }
            }
        }
        return result.sortedBy { -it.size }
    }

    override fun toString():String {
        val buff = StringBuilder(100)
        buff.append(sep).append(x.map{it.toString()}.joinToString(sep)).append("\n")
        (0 until y.size).forEach { j ->
            buff.append(y[j])
            (0 until  x.size).forEach { i ->
                buff.append(sep).append(matrix[j][i])
            }
            buff.append("\n")
        }
        return buff.toString()
    }
}
