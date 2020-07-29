package libs.sql.pool

import java.lang.Exception
import java.sql.Connection
import java.sql.PreparedStatement

interface SqlExector{
    fun statement(sql:String):SqlExector
    fun set(v:Any):SqlExector
    fun setFromList(v:List<Any>):SqlExector
    fun setFromVararg(vararg v:Any):SqlExector
    fun <V> queryTo(callback:(row:List<Pair<String,String>>)->V):List<V>
    fun queryToList():ListOfList
    fun queryToMap():ListOfMap
    fun addBatch():SqlExector
    fun exec(statementOption:Int):List<Int>
    fun exec():List<Int>
}