package libs.algorithms

import junit.framework.TestCase
import libs.json.toJson
import libs.math.round
import org.junit.Test
import java.lang.StringBuilder

class LongestCommonSubsequenceTest:TestCase() {

    @Test
    fun testLcs() {
        val x = "fuck_me_fuck_dog".toList()
        val y = "fuck_you_fuck_god".toList()
        val lcsString = LongestCommonSubsequence(x,y)
        println(lcsString)
        assertEquals(
                lcsString.findOverlappeds().toJson(),
                """[["_","f","u","c","k","_"],["f","u","c","k","_"],["f","u","c","k","_"],["f","u","c","k","_"],["_"],["o"],["u"],["u"],["_"],["_"],["_"],["g"],["o"],["d"]]"""
        )

        assertEquals(
                lcsString.findOverlappedStrings().toJson(),
                """["_fuck_","fuck_","fuck_","fuck_","_","o","u","u","_","_","_","g","o","d"]"""
        )

        assertEquals(
                lcsString.findSizeOf(5).toJson(),
                """[["f","u","c","k","_"],["f","u","c","k","_"],["_","f","u","c","k"],["f","u","c","k","_"],["_","f","u","c","k","_"]]"""
        )

        assertEquals(0.39028115875346825, LongestCommonSubsequence(x,y).similarity)

        val lcsInt = LongestCommonSubsequence(x.map { it.toInt() },y.map { it.toInt() })
        println(lcsInt)
        val sum = lcsInt.findOverlappeds{it.sum()}.sum()
        assertEquals(sum, 3214)
        val simi = LongestCommonSubsequence(x.map { it.toInt() },y.map { it.toInt() }).similarity
        assertEquals(0.39028115875346825,simi)

        val s1 = "水來自海上上海自來水"
        val s2 = "上海自來水水來自海上"
        val lcsString2 = newLcsWithStrings(s1,s2)
        println(lcsString2)
        assertEquals(lcsString2.findOverlappedStrings(), listOf("上海自來水", "水來自海上", "上", "海", "自", "來", "水", "水", "來", "自", "海", "上"))
        assertEquals(lcsString2.findLongestStrings(), listOf("上海自來水", "水來自海上"))
    }

    @Test
    fun testSimilarity() {
        assertEquals(newLcsWithStrings("fuck", "kcuf").similarity, 0.0)//0
        assertEquals(newLcsWithStrings("fuck", "fuck").similarity, 1.0)
        assertEquals(newLcsWithStrings("fuck you", "fuck you").similarity, 1.0)
        assertEquals(newLcsWithStrings("fuck you", "fuck you and me").similarity, 0.40953283969570475)
        assertEquals(newLcsWithStrings("fuck you", "fuck me").similarity, 0.48373501976820044)//0.4837
        assertEquals(newLcsWithStrings("fuck me and you", "fuck you and me").similarity, 0.5114752634696093)//0.511
        assertEquals(newLcsWithStrings("_fuckyou", "fuckyou_").similarity, 0.8188118522668012)//0.88
        assertEquals(newLcsWithStrings("_qwertyuiopasdfghjklzxcvbnm", "qwertyuiopasdfghjklzxcvbnm_").similarity, 0.9519361617179073)
    }


    @Test
    fun testLcsAndLd() {
        compare("fuck", "kcuf")
        compare("fuck", "fuck")
        compare("fuck you", "fuck you")
        compare("fuck you", "fuck you and me")
        compare("fuck you", "fuck me")
        compare("fuck me and you", "fuck you and me")
        compare("_fuckyou", "fuckyou_")
        compare("_qwertyuiopasdfghjklzxcvbnm", "qwertyuiopasdfghjklzxcvbnm_")
        compare("fuck_me_fuck_dog", "fuck_you_fuck_god")
        compare("水來自海上上海自來水", "上海自來水水來自海上")
    }


    fun compare(a:String, b:String){
        val s1 = newLcsWithStrings(a,b).similarity
        val s2 = newLdWithStrings(a,b).similarity
        println("'$a'\t'$b'\tlsc:${s1.round(3)}\tld:${s2.round(3)}")
    }

}

