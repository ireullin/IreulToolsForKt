package libs.algorithms

import java.util.*
class CosineSimilarity<T>{
    companion object {
        fun <T> of(a:Set<T>, b:Set<T>) = CosineSimilarity(a, b).calculate()
        fun <T> of(a:Map<T,Double>, b:Map<T,Double>)= CosineSimilarity(a, b).calculate()
    }

    private val union = TreeSet<T>()
    private var a:Map<T,Double>
    private var b:Map<T,Double>

    constructor(x:Map<T,Double>, y:Map<T,Double>){
        a = x
        b = y
        union.addAll(a.keys)
        union.addAll(b.keys)
    }

    constructor(x:Set<T>, y:Set<T>){
        a = x.map{ it to 1.0 }.toMap()
        b = y.map{ it to 1.0 }.toMap()
        union.addAll(a.keys)
        union.addAll(b.keys)
    }

    fun calculate():Double {
        val vecA = toVector(a)
        val vecB = toVector(b)

        val aDotB = union.indices.sumByDouble{vecA[it] * vecB[it]}
        val sumPow2A = vecA.sumByDouble{Math.pow(it, 2.0)}
        val sumPow2B = vecB.sumByDouble{Math.pow(it, 2.0)}
        val aCrossB = Math.sqrt(sumPow2A) * Math.sqrt(sumPow2B)

        return aDotB / aCrossB
    }

    private fun toVector(src:Map<T,Double>):List<Double> {
        return union.map {src.getOrDefault(it, 0.0)}
    }

    override fun toString():String {
        val sb = StringBuilder(500)

        val header = union.joinToString("\t")
        sb.append("\t").append(header).append("\n")

        val jointA = toVector(a).joinToString("\t")
        sb.append("a\t").append(jointA).append("\n")

        val jointB = toVector(b).joinToString("\t")
        sb.append("b\t").append(jointB).append("\n")

        return sb.toString()
    }
}