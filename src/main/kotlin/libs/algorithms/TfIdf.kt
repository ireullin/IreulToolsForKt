package libs.algorithms

import libs.json.toJson
import libs.math.logN

class TfIdf(logBase:Double=10.0){

    data class Term(val term:String, val tfidf:Double)

    class VerboseTerm(val term:String, val tfCount:Int, val dfCount:Int, val numTerms:Int, val numDocs:Int, val log:(Number)->Double){
        val tf = tfCount / numTerms.toDouble()
        val idf = log(numDocs/dfCount.toDouble())
        val tfidf = tf*idf
        override fun toString() = this.toJson()
        fun toShort() = Term(term, tfidf)
    }

    private val funLog = logN(logBase)
    private val docs = mutableListOf<Map<String,Int>>()


    fun addDocument(doc:List<String>):TfIdf{
        doc.groupingBy{it}.eachCount().also{addDocument(it)}
        return this
    }

    fun addDocument(doc:Map<String,Int>):TfIdf {
        docs.add(doc)
        return this
    }

    fun allTerms() = docs.flatMap{ it.keys }.toSet()

    fun evaluateWithVerbose():List<List<VerboseTerm>>{
        return docs.map{ doc ->
            val numTerms = doc.values.sum()
            doc.map { (term,tfCnt) ->
                val dfCnt = docs.filter{ it.containsKey(term) }.size
                VerboseTerm(term, tfCnt, dfCnt, numTerms, docs.size, funLog)
            }.sortedBy{ -it.tfidf }
        }
    }

    fun evaluate():List<List<Term>>{
        return docs.map{ doc ->
            val numTerms = doc.values.sum()
            doc.map { (term,tfCnt) ->
                val dfCnt = docs.filter{ it.containsKey(term) }.size
                VerboseTerm(term, tfCnt, dfCnt, numTerms, docs.size, funLog).toShort()
            }
        }
    }

    /**
     * 計算整體單詞的重要性
     * 此算法沒有依據
     * 僅供參考
     */
    fun scoreTerms():List<VerboseTerm>{
        val allTerms = allTerms()
        val rc = allTerms.map { term->
                val dfCnt = docs.filter{ it.containsKey(term) }.size
                val tfCnt = docs.map{ it[term]?:0 }.sum()
                VerboseTerm(term, tfCnt, dfCnt, allTerms.size, docs.size, funLog)
        }.sortedBy{ -it.tfidf }
        return rc
    }

    override fun toString():String {
        return docs.toJson()
    }

}



