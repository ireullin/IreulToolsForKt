package libs.tokenizers.cutters


interface Cutter {
    fun cut(target: String): Cutter

    fun toList(): List<String> {
        val buff = mutableListOf<String>()
        foreach{ i, v -> buff.add(v)}
        return buff
    }

    /**
     * 如果回傳值為null則中止該迴圈
     */
    fun <T>foreach(callback: (Int, String)->T?)
}