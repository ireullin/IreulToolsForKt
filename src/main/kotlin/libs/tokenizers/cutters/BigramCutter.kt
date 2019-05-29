package libs.tokenizers.cutters


class BigramCutter : Cutter {

    private var target: String = ""

    override fun cut(target: String): Cutter {
        this.target = target
        return this
    }


    override fun <T>foreach(callback: (Int, String)->T?) {
        if (this.target.length == 1) {
            callback(0, this.target)
            return
        }

        for (i in 0 until this.target.length - 1) {
            val subStr = this.target.substring(i, i + 2)
            if (callback(i, subStr)==null) {
                return
            }
        }
    }
}
