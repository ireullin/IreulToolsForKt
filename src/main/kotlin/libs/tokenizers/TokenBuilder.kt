package libs.tokenizers

import libs.datetime.Datetime
import libs.datetime.now
import libs.tokenizers.cutters.Cutter
import java.util.*

class TokenBuilder (sentences: List<String>){
    constructor(sentence: String):this(listOf(sentence))

    class Status(val desc:String, val time:Datetime, val status:List<String>) {
        override fun toString() = """{"time":"$time", "desc":"$desc", "status":["${status.joinToString("\",\"")}"]}"""
    }

    fun dumpStatus(desc:String="", callback:((Status)->Unit)?):TokenBuilder{
        callback?.invoke(Status(desc, now(), ArrayList(this.tokens)))
        return this
    }


    private var tokens = run{
        val buff = mutableListOf<String>()
        buff.addAll(sentences)
        buff
    }

    fun dropRedundancy(): TokenBuilder {
        // 為了保持順序一致性，所以用foreach
        val set = TreeSet<String>()
        val newTokens = ArrayList<String>(this.tokens.size)
        for (token in this.tokens) {
            if (set.contains(token))
                continue

            newTokens.add(token)
            set.add(token)
        }
        this.tokens = newTokens
        return this
    }

    fun map(callback: (Int, String)->String): TokenBuilder {
        val newTokens = ArrayList<String>(this.tokens.size)
        for (i in this.tokens.indices) {
            newTokens.add(callback(i, tokens[i]))
        }
        this.tokens = newTokens
        return this
    }

    fun cutBy(cutter: Cutter): TokenBuilder {
        val newTokens = ArrayList<String>(this.tokens.size)
        for (token in this.tokens) {
            cutter.cut(token).foreach({ i, subToken ->
                newTokens.add(subToken)
                true
            })
        }
        this.tokens = newTokens
        return this
    }

    fun dropIf(callback: (Int, String)->Boolean): TokenBuilder {
        val newTokens = ArrayList<String>(this.tokens.size)
        for (i in this.tokens.indices) {
            val token = this.tokens[i]
            if (!callback(i, token))
                newTokens.add(token)
        }
        this.tokens = newTokens
        return this
    }

    fun tokens(): List<String> {
        return this.tokens
    }

    fun peek(msg: (List<String>)->Unit): TokenBuilder {
        msg(tokens)
        return this
    }
}
