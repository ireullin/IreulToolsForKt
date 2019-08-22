package libs.json

import junit.framework.TestCase
import org.junit.Test

class NodeTest:TestCase(){

    @Test
    fun testDouble(){
        val n = Node.ofMap(sample())
        println(n["contents"][1]["price"].toDouble())

    }

    /**
     * 測試string輸出會不會夾帶特殊符號
     */
    @Test
    fun testString(){
        val n = Node.ofMap(sample())
        assertEquals(n["shopperName"].toString(),"John Smith")
        assertEquals(n["shopperName"].toJson(),"\"John Smith\"")
    }

    @Test
    fun testIndexAndNull(){
        val n = Node.ofMap(sample())
        assertEquals(n["contents"][1]["productName"].toString(), "WonderWidget")
        assertEquals(n["contents"][-1]["productName"].toString(), "WonderWidget")
        assertEquals(n["contents"][2].isNull, true)
        assertEquals(n["contents"][2].toString(),"null")
        assertEquals(n["contents"][2].toInt(-1),-1)
//        ["productName"]
    }

    @Test
    fun testMapAndList(){
        val n = Node.ofMap(sample())

        val nameToIds = n["contents"].toMapNotNull{(_,v)-> v["productName"].toString to v["productID"].toString}
        assertEquals(nameToIds.toJson(),"""{"SuperWidget":"34","WonderWidget":"56"}""")

        val ids = n["contents"].toListNotNull{(_,v)-> v["productID"].toString }
        assertEquals(ids.toJson(),"""["34","56"]""")
    }

    @Test
    fun testNode() {
        val n = Node.ofMap(sample())
        n.forEach{(k,v)->
            when{
                v.isMap -> println("$k is a map")
                v.isArray -> println("$k is a array")
                else -> println("$k $v")
            }
        }
        println("=========================================")
        n["contents"].forEach{println(it)}
        println("=========================================")
        val afterMapMap = n.toMap{(k,v) ->
            when{
                v.isMap -> k to "$k is a map"
                v.isArray -> k to "$k is a array"
                else -> k to "nothing"
            }
        }
        println(afterMapMap)
        println("=========================================")
        println( n["contents"][4]["productName"]["fuck"]?:"fuck" )
    }


    fun sample() = """
{
    "orderID": 12345,
    "shopperName": "John Smith",
    "shopperEmail": "johnsmith@example.com",
    "contents": [
    {
        "productID": 34,
        "productName": "SuperWidget",
        "quantity": 1,
        "price": 1.1
    },
    {
        "productID": 56,
        "productName": "WonderWidget",
        "quantity": 3,
        "price": 0.99
    }
    ],
    "orderCompleted": true
}
"""
}

