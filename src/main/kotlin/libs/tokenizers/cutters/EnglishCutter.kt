package libs.tokenizers.cutters

import libs.tokenizers.isEnglishChar

class EnglishCutter : Cutter {

    private var target: String = ""

    override  fun cut(target: String): Cutter {
        this.target = target
        return this
    }

    override fun <T>foreach(callback: (Int, String)->T?) {
        if(target.length<1){
            return
        }

        var isInEnglish = isEnglishChar(target[0])
        var offset = 0
        var index = 0
        for (i in 0 until target.length) {
            if (isInEnglish == isEnglishChar(target[i]))
                continue

            val newToken = target.substring(offset, i)
            if (callback(index, newToken)==null) {
                return
            }
            index++
            isInEnglish = !isInEnglish
            offset = i
        }

        val newToken = target.substring(offset)
        callback(index, newToken)
    }
}
