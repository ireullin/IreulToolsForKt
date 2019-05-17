package libs.math

/**
 * https://www.ycc.idv.tw/confusion-matrix.html
 */
class ConfusionMatrix(private var truePositive:Int, private var trueNegative:Int,
                      private var falsePositive:Int, private var falseNegative:Int){

    constructor():this(0,0,0,0)

    fun incTp(){truePositive++}
    fun incFp(){falsePositive++}
    fun incTn(){trueNegative++}
    fun incFn(){falseNegative++}

    val tp get() = truePositive
    val fp get() = falsePositive
    val tn get() = trueNegative
    val fn get() = falseNegative

    val accuracy by lazy { (tn+tp).toDouble() / (tn+tp+fn+fp) }

    /**
     * Precision看的是在預測正向的情形下，實際的「精準度」是多少
     */
    val precision by lazy {truePositive / (truePositive+falsePositive).toDouble()}

    /**
     * recall則是看在實際情形為正向的狀況下，預測「能召回多少」正向的答案
     */
    val recall by lazy {truePositive / (truePositive+falseNegative).toDouble()}


    val sensitive by lazy { truePositive / (truePositive+falseNegative) }
    val specificity by lazy { trueNegative / (trueNegative+falsePositive) }
    val prevalence by lazy { (fn+tp).toDouble() / (tn+tp+fn+fp) }

    val errorRate get() = 1-accuracy

    /**
     * 如果今天我覺得Precision和Recall都同等重要，我想要用一個指標來統合標誌它，這就是F1 Score或稱F1 Measure，它是F Measure的一個特例，當belta=1時就是F1 Measure，代表Precision和Recall都同等重要，那如果我希望多看中一點Precision，那belta就可以選擇小一點，當belta=0時，F Measure就是Precision；如果我希望多看中一點Recall，那belta就可以選擇大一點，當belta無限大時，F Measure就是Recall。
     */
    val f1Score get() =  fMeasure()

    /**
     * 如果今天我覺得Precision和Recall都同等重要，我想要用一個指標來統合標誌它，這就是F1 Score或稱F1 Measure，它是F Measure的一個特例，當belta=1時就是F1 Measure，代表Precision和Recall都同等重要，那如果我希望多看中一點Precision，那belta就可以選擇小一點，當belta=0時，F Measure就是Precision；如果我希望多看中一點Recall，那belta就可以選擇大一點，當belta無限大時，F Measure就是Recall。
     */
    fun fMeasure(beta:Double=1.0):Double{
        val pow2Beta = beta*beta
        return (1 + pow2Beta )*precision*recall / ((pow2Beta*precision)+recall)
    }

    override fun toString():String {
        return """
            |
            |┌────┬─────┬─────┐
            |│        │real 1    │real 0    │
            |├────┼─────┼─────┤
            |│guess 1 │tp:${tp.toString().padEnd(7)}│fp:${fp.toString().padEnd(7)}│
            |├────┼─────┼─────┤
            |│guess 0 │fn:${fn.toString().padEnd(7)}│tn:${tn.toString().padEnd(7)}│
            |└────┴─────┴─────┘
            |accuracy:$accuracy
            |precision:$precision
            |recall:$recall
            |f1Score:$f1Score
            |""".trimMargin()
    }
}