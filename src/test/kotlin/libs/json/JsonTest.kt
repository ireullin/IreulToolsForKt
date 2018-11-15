package libs.json


import junit.framework.TestCase
import org.junit.Test

class JsonTest:TestCase(){
    data class ForTest(val a:Int=0, val b:Int=0, val c:Int=0)

    @Test
    fun testNode() {
        val json = Json()

        val x = mapOf("a" to 1, "b" to 2, "c" to 3)
        val y = listOf("a","b","c")
        val z = json.stringify(mapOf("x" to x, "y" to y))
        println(z)
        val zNode = json.toNode(z)

        val xBar:Map<String,Int> = json.toMap(zNode["x"])
        assertEquals(x, xBar)

        val xToClass = json.toClass<ForTest>(zNode["x"])
        assertEquals(xToClass, ForTest(1,2,3))

        val yBar:List<String> = json.toList(zNode["y"])
        assertEquals(y, yBar)
    }

    @Test
    fun testClass() {
        // 要被反序列化的class必須要有無參數建構式
        // 或是參數要有預設值
        val target = ForTest(1, 2, 3)
        val src = Json.toClass<ForTest>(target.toJson())
        assertEquals(src, target)
    }

    @Test
    fun testMap() {
        val target = mapOf("a" to 1, "b" to 2, "c" to 3)
        val src = Json.toMap<String,Int>(target.toJson())
        assertEquals(src, target)
    }

    @Test
    fun testList() {
        val target = listOf("a","b","c")
        val src = Json.toList<String>(target.toJson())
        assertEquals(src, target)
    }
}
