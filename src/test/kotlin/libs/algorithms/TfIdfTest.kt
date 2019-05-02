package libs.algorithms

import junit.framework.TestCase
import libs.json.toJson
import org.junit.Test

class TfIdfTest:TestCase() {
    @Test
    fun test1() {
        val a = "john is very very tall".split(" ")
        val b = "mary is fucking very very fat".split(" ")
        val tfIdf = TfIdf().addDocument(a).addDocument(b)

        println( tfIdf.scoreTerms() )
        println( tfIdf.evaluateWithVerbose() )
        println( tfIdf.evaluate().toJson() )

    }
}