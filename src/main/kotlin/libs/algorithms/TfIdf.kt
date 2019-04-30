package libs.algorithms

import libs.json.toJson
import libs.math.logN

class TfIdf(logBase:Double=10.0){

    class Term(val term:String, val tfCount:Int, val dfCount:Int, val numTerms:Int, val numDocs:Int, log:(Number)->Double){
        val tf = tfCount / numTerms.toDouble()
        val idf = log(numDocs/dfCount.toDouble())
        val tfidf = tf*idf
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

    fun evaluate():List<List<Term>>{
        return docs.map{ doc ->
            doc.map { (term,tfCnt) ->
                val dfCnt = docs.filter{ it.containsKey(term) }.size
                val numTerms = doc.values.sum()
                Term(term, tfCnt, dfCnt, numTerms, docs.size, funLog)
            }
        }
    }

    override fun toString():String {
        return docs.toJson()
    }

}



