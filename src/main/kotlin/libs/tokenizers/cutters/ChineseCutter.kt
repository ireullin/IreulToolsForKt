package libs.tokenizers.cutters

import libs.tokenizers.isChineseChar

class ChineseCutter(private val childCutter:Cutter):Cutter{
    constructor():this(NothingCutter())

    private var target: String = ""

    override fun cut(target: String): Cutter {
        this.target = target
        return this
    }

    override fun <T>foreach(callback: (Int, String)->T?) {
        if(target.length<1){
            return
        }

        var isInChinese = isChineseChar(target[0])
        var offset = 0
        var index = 0
        for (i in 0 until target.length) {
            if (isInChinese == isChineseChar(target[i]))
                continue

            val newToken = target.substring(offset, i)
            val childNewTokens = execChileCutter(isInChinese, newToken)
            for (childNewToken in childNewTokens) {
                if (callback(index, childNewToken)==null) {
                    return
                }
                index++
            }

            isInChinese = !isInChinese
            offset = i
        }

        val newToken = target.substring(offset)
        val childNewTokens = execChileCutter(isInChinese, newToken)
        for (childNewToken in childNewTokens) {
            if (callback(index, childNewToken)==null) {
                return
            }
            index++
        }
    }

    private fun execChileCutter(isInChinese: Boolean, token: String): List<String> {
        return if (isInChinese) {
            this.childCutter.cut(token).toList()
        } else {
            listOf(token)
        }
    }
}
