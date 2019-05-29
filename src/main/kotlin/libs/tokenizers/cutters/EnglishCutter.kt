package libs.tokenizers.cutters

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

        var isInEnglish = isEnglish(target[0])
        var offset = 0
        var index = 0
        for (i in 0 until target.length) {
            if (isInEnglish == isEnglish(target[i]))
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

    companion object {
        fun isEnglish(c: Char): Boolean {
            return when{
                (c >= 'a' && c <= 'z') -> true
                (c >= 'A' && c <= 'Z') -> true
                else -> false
            }
        }

        fun isAllEnglish(s: String, ignoreChars:Set<Char> = setOf()): Boolean {
            try {
                for (c in s.toCharArray()) {
                    if (c in ignoreChars) continue
                    if (isEnglish(c)) continue
                    return false
                }
                return true
            } catch (e: Exception) {
                return false
            }
        }
    }
}
