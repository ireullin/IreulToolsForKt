package libs.json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.treeToValue

class Json(val parserInstance:ObjectMapper){
    companion object {
        fun stringify(obj:Any) = Json().stringify(obj)
        fun toNode(s:String) = Json().toNode(s)
        fun <K,V> toMap(s:String):Map<K,V> = Json().toMap(s)
        fun <K,V> toMap(n:JsonNode):Map<K,V> = Json().toMap(n)
        fun <V> toList(s:String):List<V> = Json().toList(s)
        fun <V> toList(n:JsonNode):List<V> = Json().toList(n)
        inline fun <reified V:Any> toClass(s:String):V = Json().toClass<V>(s)
        inline fun <reified V:Any> toClass(n:JsonNode):V = Json().toClass<V>(n)
    }

    constructor():this(ObjectMapper())

    fun stringify(obj:Any) = parserInstance.writeValueAsString(obj)

    fun toNode(s:String) = parserInstance.readTree(s)

    fun <K,V> toMap(s:String):Map<K,V> = parserInstance.readValue(s)
    fun <K,V> toMap(n:JsonNode):Map<K,V> = parserInstance.treeToValue(n)

    fun <V> toList(s:String):List<V> = parserInstance.readValue(s)
    fun <V> toList(n:JsonNode):List<V> = parserInstance.treeToValue(n)

    inline fun <reified V:Any> toClass(s:String):V = parserInstance.readValue(s, V::class.java)
    inline fun <reified V:Any> toClass(n:JsonNode):V = parserInstance.treeToValue(n, V::class.java)
}

fun Any.toJson() = Json.stringify(this)
