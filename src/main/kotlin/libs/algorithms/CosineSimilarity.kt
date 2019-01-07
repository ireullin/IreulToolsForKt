package libs.algorithms

import java.util.*

class CosineSimilarity<T>(private val a:Set<T>, private val b:Set<T>) {
    companion object {
        fun <T> of(a:Set<T>, b:Set<T>) = CosineSimilarity(a, b).calculate()
    }

    private val union = TreeSet<T>().apply {
        addAll(a)
        addAll(b)
    }

    fun calculate():Double? {
        val vecA = toVector(a)
        val vecB = toVector(b)

        val aDotB = union.indices.sumByDouble{vecA[it] * vecB[it]}
        val sumPow2A = vecA.sumByDouble{Math.pow(it, 2.0)}
        val sumPow2B = vecB.sumByDouble{Math.pow(it, 2.0)}
        val aCrossB = Math.sqrt(sumPow2A) * Math.sqrt(sumPow2B)

        return aDotB / aCrossB
    }

    private fun toVector(src:Set<T>):List<Double> {
        return union.map {
            if(src.contains(it)){1.0}
            else {0.0}
        }
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
