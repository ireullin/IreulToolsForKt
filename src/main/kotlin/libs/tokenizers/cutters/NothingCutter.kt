package libs.tokenizers.cutters

class NothingCutter : Cutter {

    private var target: String = ""

    override fun cut(target: String): Cutter {
        this.target = target
        return this
    }

    override fun <T>foreach(callback: (Int, String)->T?) {
        callback(0, this.target)
    }
}