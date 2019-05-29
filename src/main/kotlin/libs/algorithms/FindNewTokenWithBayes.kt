package libs.algorithms

import kotlin.math.min

/**
 * https://github.com/HaishuoFang/Find_New_token/blob/master/find_new_word.py
 * https://spaces.ac.cn/archives/4256/comment-page-1#comments
 */
class FindNewTokenWithBayes(val doc:List<String>){

    data class Result(val name:String, val score:Int)

    private var verbose: ((String)->Unit)? = null
    private var maxTokenLength = 4
    private var minTf = 25
    private val minProb = mutableMapOf(2 to 5.0, 3 to 25.0, 4 to 125.0)

    private fun getVerbose():((String)->Unit)?{return verbose}

    /**
     * 是否顯示計算過程
     */
    fun showVerbose(callback:(String)->Unit):FindNewTokenWithBayes{
        verbose = callback
        return this
    }

    /**
     * 基於多長的詞進行機率計算
     * 此參數不代表最後切出來詞的長度
     * 最後切出來的詞可能會比這個略長一點
     * 預設 4
     */
    fun maxTokenLength(value:Int):FindNewTokenWithBayes{
        maxTokenLength = value
        return this
    }

    /**
     * 詞頻高於此值才列入參考
     * 預設 25
     */
    fun minTf(value:Int):FindNewTokenWithBayes{
        minTf = value
        return this
    }

    /**
     * 設定每個長度的詞的機率門檻
     */
    fun probThreshold(tokenLength:Int, prob:Double):FindNewTokenWithBayes{
        minProb.put(tokenLength, prob)
        return this
    }

    /**
     * 重設每個長度的詞的機率門檻
     */
    fun resetProbThreshold():FindNewTokenWithBayes{
        minProb.clear()
        return this
    }


    fun statistics():List<List<Result>>{
        return doc.flatMap{slice(it, 1, maxTokenLength)}
                .groupingBy{it}
                .eachCount()
                .map{(k,v)->Result(k,v)}
                .groupBy{it.name.length}
                .values
                .map{ it.sortedByDescending{it.score} }
    }


    fun calculate():Map<String,Result>{
        // 將所有的詞以 1~tokenLength grams的方式切開，只取有一定出現比例的
        val ngrams = doc
                .flatMap{slice(it,1,maxTokenLength)}
                .groupingBy{it}
                .eachCount()
                .filter{it.value>=minTf}
        getVerbose()?.invoke("filter tokens with tf=$ngrams")
        getVerbose()?.invoke("")

        // 所有字的總數
        val numChar = ngrams.size// ngrams.filter{(k,_)->k.length==1}.values.sum()


        // 每個長度的詞只拿大於一定分數之上的
        // 產生一份可能是新詞機率比大的字典檔
        val ngramsDict = ngrams.keys
                .filter{ calculateProb(it, numChar, ngrams) }
                .toSet()

        getVerbose()?.invoke("")
        getVerbose()?.invoke("base tokens=$ngramsDict")
        getVerbose()?.invoke("")

        val tokens = doc.filter{it.length>2}
                // 產生類似lcs的積分表 並依據此切詞
                .flatMap{ cutSentence(it, ngramsDict) }
                .groupingBy{it}.eachCount()
                .filter{ it.value>=minTf }
                .filter{ isReal(it.key, ngramsDict) }
                .mapValues{Result(it.key, it.value)}

        return tokens
    }


    /**
     * 為了確保該單詞不是被贅字所串起來的
     */
    private fun isReal(token:String, ngramsDict:Set<String>):Boolean{
        if(token.length<3)
            return true

        for(s in slice(token, 3, maxTokenLength)){
            if(s !in ngramsDict) {
                return false
            }
        }

        return true
    }

    private fun cutSentence(sentence:String, ngramsDict:Set<String>):List<String>{
        val mask = IntArray(sentence.length){0}

        // 將句子拆成各種組合
        sentence.forEachIndexed{offset,_->
            (2..maxTokenLength).forEach {step->
                var end = min(offset+step, sentence.length)
                val seg = sentence.substring(offset, end)
                if(seg in ngramsDict) {
                    (offset until end).forEach {i->
                        mask[i] += 1
                    }
                }
            }
        }

        getVerbose()?.invoke(sentence.toCharArray().joinToString(" "))
        getVerbose()?.invoke(mask.toList().joinToString("") {it.toString().padEnd(3, ' ')})
        getVerbose()?.invoke("")

        var sb = StringBuilder(10)
        val buff = mutableListOf<String>()
        sentence.forEachIndexed{i,c->
            if(mask[i]>0){
                sb.append(c)
            }else{
                if(sb.isNotBlank()){
                    buff.add(sb.toString())
                    sb = StringBuilder(10)
                }
            }
        }

        if(sb.isNotBlank()){
            buff.add(sb.toString())
        }
        return buff
    }

    /**
     *  將字串組成各種組合
     */
    private fun slice(s:String, shortest:Int, longest:Int):List<String>{
        val buff = mutableListOf<String>()
        (0 until s.length).forEach {offset->
            (shortest..longest).forEach {step->
                var end = offset+step
                if(end<=s.length){
                    val seg = s.substring(offset, end)
                    buff.add(seg)
                }
            }
        }
        return buff
    }


    private fun calculateProb(token:String, numChar:Int, ngrams:Map<String,Int>):Boolean{
        if(token.length<2)
            return false

        // 有可能應該是要轉成double
        val logBuff = if(getVerbose()==null){null}else{StringBuilder()}
        val minScore = token.dropLast(1).mapIndexed{i,_->
            val fromStart = token.substring(0, i+1)
            val toTail = token.substring(i+1)
            /**
             * p(ABC) = ngrams[token] / numChar
             * p(A) = ngrams[fromStart] / numChar
             * p(BC) = ngrams[toTail] / numChar
             * score = p(ABC) / (p(A)*p(BC))
             **/
            val score = (numChar*(ngrams[token]?:0)) / ((ngrams[fromStart]?.toDouble()?:0.0000000001 ) * (ngrams[toTail]?.toDouble()?:0.0000000001))
            logBuff?.append("[prefix=$fromStart(${ngrams[fromStart]}) suffix=$toTail(${ngrams[toTail]}) score=$score]")
            score
        }.min()?:0.0


        val hasKept = minScore > minProb[token.length]!!
        getVerbose()?.invoke( "token=$token(${ngrams[token]}) keep=$hasKept $logBuff")

        return hasKept
    }
}
