package libs.tokenizers.cutters

/**
 * 遇見特殊符號便從該符號將句子斷開
 * 並且丟棄該符號
 */
class GarbageCutter(symbols:Set<Char> = GarbageCutter.DEFAULT_SYMBOLS): Cutter {

    private var target: String = ""
    private var allSymbols = symbols

    fun addSymbols(newSymbols:Set<Char>):GarbageCutter{
        allSymbols += newSymbols
        return this
    }

    fun addSymbols(newSymbols:String) = addSymbols(newSymbols.toSet())
    fun addSymbols(vararg newSymbols:Char) = addSymbols(newSymbols.toSet())

    override fun cut(target: String): Cutter {
        this.target = target
        return this
    }

    private fun isGarbage(c:Char) = c in allSymbols

    override fun <T>foreach(callback: (Int, String)->T?) {
        var offset = 0
        var index = 0
        for (i in 0 until this.target.length) {
            val current = this.target[i]
            if (isGarbage(current)) {
                val rc = callback(index, this.target.substring(offset, i))
                offset = i + 1
                index++
                if (rc==null) {
                    return
                }
            }
        }
        callback(index, this.target.substring(offset))
    }

    override fun toString(): String {
        val sb = StringBuilder(100)
        for (i in 0 until this.target.length) {
            val current = this.target[i]
            sb.append(current)
                    .append("(")
                    .append(if (isGarbage(current)) "y" else "n")
                    .append(")")
                    .append("\n")
        }

        return sb.toString()
    }

    companion object {
        val DEFAULT_SYMBOLS = setOf('\ufeff', '[', ']', '／', '\'', '+', ')', '(', '〔', '〕', '【', '】', '【', '】', '。', '，', '±', '/', '(', '、', '「', '」', '~', '★', '》', '《', '］', '▁', '▂', '▃', '▄', '▅', '▆', '▇', '█', '▋', '▌', '◣', '◢', '◤', '◥', '⊿', '▲', '△', '▼', '▽', '（', '）', '*', '◆', '|', '│', '_', ',')
    }
}
