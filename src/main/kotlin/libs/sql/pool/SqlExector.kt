package libs.sql.pool

import java.lang.Exception
import java.sql.Connection

interface SqlExector:AutoCloseable{
    fun statement(sql:String):SqlExector
    fun <V> set(v:V):SqlExector
    fun <V> queryTo(callback:(row:List<Pair<String,String>>)->V):List<V>
    fun queryToList():ListOfList
    fun queryToMap():ListOfMap
}