package libs.algorithms

import junit.framework.TestCase
import libs.json.toJson
import org.junit.Test

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

        assertEquals(0.667279411764706, lcsSimilarity(x,y))

        val lcsInt = LongestCommonSubsequence(x.map { it.toInt() },y.map { it.toInt() })
        println(lcsInt)
        val sum = lcsInt.findOverlappeds{it.sum()}.sum()
        assertEquals(sum, 3214)
        val simi = lcsSimilarity(x.map { it.toInt() },y.map { it.toInt() })
        assertEquals(0.667279411764706,simi)

        val s1 = "水來自海上上海自來水"
        val s2 = "上海自來水水來自海上"
        val lcsString2 = newLcsWithStrings(s1,s2)
        println(lcsString2)
        assertEquals(lcsString2.findOverlappedStrings(), listOf("上海自來水", "水來自海上", "上", "海", "自", "來", "水", "水", "來", "自", "海", "上"))
        assertEquals(lcsString2.findLongestStrings(), listOf("上海自來水", "水來自海上"))
    }

    @Test
    fun testSimilarity() {
        assertEquals(lcsSimilarity("fuck", "kcuf"), 0.0)
        assertEquals(lcsSimilarity("fuck", "fuck"), 1.0)
        assertEquals(lcsSimilarity("fuck you", "fuck you"), 1.0)
        assertEquals(lcsSimilarity("fuck you", "fuck you and me"), 0.7666666666666666)
        assertEquals(lcsSimilarity("fuck you", "fuck me"), 0.6696428571428572)
    }

    @Test
    fun testSimilarity2() {
        assertEquals(lcsSimilarity2("fuck", "kcuf"), 0.25)
        assertEquals(lcsSimilarity2("fuck", "fuck"), 1.0)
        assertEquals(lcsSimilarity2("fuck you", "fuck you"), 1.0)
        assertEquals(lcsSimilarity2("fuck you", "fuck you and me"), 0.2933333333333333)
        assertEquals(lcsSimilarity2("fuck you", "fuck me"), 0.40625)
    }

}

