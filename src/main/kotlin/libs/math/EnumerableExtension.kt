package libs.math

fun List<Number>.toDoubleList() = this.map { it.toDouble() }

/**
 * 加總
 */
fun List<Double>.sum() = this.sumByDouble{it}

/**
 * 幾何平均
 */
fun List<Double>.geoMean() = Math.sqrt(this.sumByDouble{it*it})

/**
 * 中位數
 */
fun List<Double>.median():Double{
    val sorted = this.sortedBy{it}
    val i = sorted.size / 2
    return if(sorted.size%2==1){
         sorted[i]
    }else{
         (sorted[i-1]+sorted[i])/2
    }
}


data class SumAndMean(val sum:Double, val mean:Double)

/**
 * 加總與平均
 */
fun List<Double>.sumAndMean():SumAndMean{
    val sum = this.sum()
    val mean = sum / this.size
    return SumAndMean(sum, mean)
}

data class Statistics(val sum:Double, val mean:Double, val variance:Double, val stdev:Double, val ddof:Int)

/**
 * 計算變異數 標準差 總和與平均
 */
fun List<Double>.statistical(ddof:Int=1):Statistics{
    val sm = this.sumAndMean()
    val leastSqrt = this.sumByDouble { Math.pow(it-sm.mean, 2.0) }
    val variance = leastSqrt / (this.size-ddof)
    val stdev = Math.sqrt(variance)
    return Statistics(sm.sum, sm.mean, variance, stdev, ddof)
}

/**
 * 協方差
 */
fun List<Double>.covariance(t:List<Double>, ddof:Int=1):Double{
    require(t.size==this.size)
    val xBar = this.sumAndMean().mean
    val yBar = t.sumAndMean().mean
    var accum = (0 until t.size).sumByDouble{ (this[it]-xBar)*(t[it]-yBar) }
    return accum / (this.size - ddof)
}

/**
 * 皮爾森係數
 */
fun List<Double>.pearson(t:List<Double>, ddof:Int=1):Double{
    val cov = this.covariance(t,ddof)
    val xStdev = this.statistical(ddof).stdev
    val yStdev = t.statistical(ddof).stdev
    return cov / (xStdev*yStdev)
}


fun List<Double>.normalizeByL1():List<Double>{
    val sum = this.sum()
    return this.map { it/sum }
}

fun List<Double>.normalizeByL2():List<Double>{
    val sum = this.geoMean()
    return this.map { it/sum }
}

/**
 * 計算 熵
 */
fun <T> List<T>.entropy():Double{
    val counter = this.groupingBy { it }.eachCount()
    val entropy = counter.values.sumByDouble {
        val p = it.toDouble() / this.size
        (-p)*log2(p)
    }
    return entropy
}

/**
 * 計算 交叉熵
 */
fun <T> List<T>.concitionalEntropyWith(label:List<T>):Double{
    require(this.size==label.size)

    val dataset = mutableMapOf<T,MutableList<T>>()
    this.forEachIndexed{i, ele ->
        dataset.getOrPut(ele){mutableListOf()}.add(label[i])
    }

    val concitionalEntropy = dataset.values.sumByDouble {
        (it.size.toDouble() / this.size) * it.entropy()
    }

    return concitionalEntropy
}
