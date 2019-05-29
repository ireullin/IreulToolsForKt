package libs.tokenizers.cutters

import kotlin.math.min


class NgramCutter(val n:Int) : Cutter {
    init{require(n>0)}
    private var target: String = ""

    override fun cut(target: String): Cutter {
        this.target = target
        return this
    }


    override fun <T>foreach(callback: (Int, String)->T?) {
        if (target.length == 1) {
            callback(0, this.target)
            return
        }
        for (i in 0 until target.length - 1) {
            val e = min(i+n, target.length)
            if (callback(i, target.substring(i,e))==null) {
                return
            }
        }
    }
}
