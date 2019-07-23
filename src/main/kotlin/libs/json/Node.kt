package libs.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue


class Node private constructor(val om:ObjectMapper, private val obj:Any?){
    companion object {
        fun ofMap(s:String):Node{
            val om = ObjectMapper()
            val n:Map<String,Any> = om.readValue(s)
            return Node(om,n)
        }
        fun ofArray(s:String):Node{
            val om = ObjectMapper()
            val n:List<Any> = om.readValue(s)
            return Node(om,n)
        }
    }

    val isNull = obj==null

    val isArray:Boolean
        get() =  obj is List<*>

    val isMap:Boolean
        get() =  obj is Map<*,*>

    val isValue:Boolean
        get() = !isArray && !isMap && !isNull

    val size:Int get()=
        when {
            isArray -> toList().size
            isMap -> toMap().size
            else -> throw Exception("it is not a list or map")
        }

    operator fun get(k:String):Node{
        if(isNull)
            return Node(om,null)

        val o = obj as Map<String,Any>
        return Node(om, o[k])
    }

    operator fun get(i:Int):Node{
        val o = obj as List<Any>
        return when{
            i<0 -> Node(om, o[ o.size+i])
            i>=o.size -> Node(om,null)
            else -> Node(om, o[i])
        }
    }

    fun forEach(callback:(Pair<String,Node>)->Unit){
        when{
            isArray -> toList().forEachIndexed{i,v-> callback.invoke(Pair(i.toString(),Node(om,v)))}
            isMap -> toMap().forEach{k,v-> callback.invoke(Pair(k,Node(om,v)))}
            else -> throw Exception("it is not a list or map")
        }
    }

    val toString get() = toString()

    override fun toString():String{
        return obj.toString()
    }

    fun toString(default:String):String{
        if(isNull) return default
        return toString()
    }

    fun toJson():String{
        return om.writeValueAsString(obj)
    }

    fun toDouble():Double{
        return toString().toDouble()
    }

    fun toDouble(default:Double):Double{
        if(isNull) return default
        return toString().toDouble()
    }

    fun toInt():Int{
        return toString().toInt()
    }

    fun toInt(default:Int):Int{
        if(isNull) return default
        return toString().toInt()
    }

    fun toBoolean():Boolean{
        return toString().toBoolean()
    }

    fun toMap():Map<String,Any>{
        return obj as Map<String,Any>
    }

    fun <T,R>toMap(callback:(Pair<String,Node>)->Pair<T,R>):Map<T,R>?{
        return when{
            isArray -> toList().mapIndexed{i,v-> callback.invoke(Pair(i.toString(),Node(om,v)))}
            isMap -> toMap().map{(k,v)-> callback.invoke(Pair(k,Node(om,v)))}
            isNull -> null
            else -> throw Exception("it is not a list or map")
        }?.toMap()
    }

    /**
     * 若node為null回傳一個空的map
     */
    fun <T,R>toMapNotNull(callback:(Pair<String,Node>)->Pair<T,R>):Map<T,R>{
        return toMap(callback)?: mapOf()
    }

    fun toList():List<Any>{
        return obj as List<Any>
    }

    fun <T>toList(callback:(Pair<String,Node>)->T):List<T>?{
        return when{
            isArray -> toList().mapIndexed{i,v-> callback.invoke(Pair(i.toString(),Node(om,v)))}
            isMap -> toMap().map{(k,v)-> callback.invoke(Pair(k,Node(om,v)))}
            isNull -> null
            else -> throw Exception("it is not a list or map")
        }
    }

    /**
     * 若node為null回傳一個空的list
     */
    fun <T>toListNotNull(callback:(Pair<String,Node>)->T):List<T>{
        return toList(callback)?: listOf()
    }

    fun also(callback:(Node)->Unit):Node{
        callback(this)
        return this
    }
}

