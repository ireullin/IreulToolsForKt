package libs.sql.connections

import java.sql.Connection

typealias ListOfStringMap = List<Map<String,String>>
typealias ListOfStringList = List<List<String>>

@Deprecated(message = "Deprecated class")
interface SqlConnection:AutoCloseable{
    fun <T> queryIndexed(cmd:String, callback:(Int, SqlRow)->T?):List<T>
    fun <T> query(cmd:String, callback:(SqlRow)->T?):List<T>
    fun queryToMap(cmd:String):ListOfStringMap
    fun queryToList(cmd:String):ListOfStringList
    fun execMutiCommands(cmd:String)
    val javaSqlConnection:Connection
    val isValid:Boolean
}