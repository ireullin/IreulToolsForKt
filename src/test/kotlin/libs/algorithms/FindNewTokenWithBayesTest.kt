package libs.algorithms

import junit.framework.TestCase
import libs.math.median
import libs.math.statistical
import libs.tokenizers.TokenBuilder
import libs.tokenizers.cutters.ChineseCutter
import libs.tokenizers.cutters.EnglishCutter
import libs.tokenizers.cutters.GarbageCutter
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class FindNewTokenWithBayesTest:TestCase(){

    @Test
    fun test1() {
        val path = Paths.get("").toAbsolutePath().parent.parent.toString()+"/testData/一年战争.txt"
        val lines = File(path).readLines()
        val tb = TokenBuilder(lines,true).saveStatus("init")
                .cutBy(ChineseCutter()).saveStatus("cut by chinese")
                .cutBy(GarbageCutter()).saveStatus("cut by garbage")
                .dropIfMatch{i,v->v.isBlank()}.saveStatus("drop blank")

        tb.statuses().forEach{println(it)}

        val model = FindNewTokenWithBayes(tb.tokens())
        val stat = model.statistics()
        stat.forEach{
            println(it.map {it.name})
            print( it.map{it.score.toDouble()}.statistical() )
            println( "median="+it.map{it.score.toDouble()}.median() )
            println()
        }

        val dict = model
                .showVerbose{ log -> println(log) }
                .minTf(2)
                .maxTokenLength(4)
                .probThreshold(2,1.0)
                .probThreshold(3,1.0)
                .probThreshold(4,1.0)
                .probThreshold(5,1.0)
                .calculate()
                .values
                .filterNot{ isNumeric(it.name) }
                .filterNot{ EnglishCutter.isAllEnglish(it.name) }
                .sortedBy{it.score}
                .map{listOf(it.name, it.score)}

        println(dict)
    }

    fun isNumeric(strNum:String):Boolean {
        return strNum.matches("-?\\d+(\\.\\d+)?".toRegex())
    }


}