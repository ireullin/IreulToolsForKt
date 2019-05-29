package libs.tokenizers.cutters

class ChineseCutter(cutter:Cutter):Cutter{
    constructor():this(NothingCutter())

    private var target: String = ""
    private val childCutter = cutter

    override fun cut(target: String): Cutter {
        this.target = target
        return this
    }

    override fun <T>foreach(callback: (Int, String)->T?) {
        if(target.length<1){
            return
        }

        var isInChinese = isChinese(target[0])
        var offset = 0
        var index = 0
        for (i in 0 until target.length) {
            if (isInChinese == isChinese(target[i]))
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

    companion object {
//        fun containsHanScript(s:String):Boolean {
//            return s.codePoints().anyMatch {codepoint-> Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN}
//        }

        fun isAllChinese(s: String): Boolean {
            try {
                for (c in s.toCharArray()) {
                    if (!isChinese(c))
                        return false
                }
                return true
            } catch (e: Exception) {
                return false
            }

        }

        // 根据Unicode编码完美的判断中文汉字和符号
        fun isChinese(c: Char): Boolean {
            val ub = Character.UnicodeBlock.of(c)
            return if (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                    || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                    || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION) {
                true
            } else false
        }
    }
}
