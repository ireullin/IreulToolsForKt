package libs.sql.connections

typealias ListOfStringMap = List<Map<String,String>>
typealias ListOfStringList = List<List<String>>
interface SqlConnection:AutoCloseable{
    fun <T> queryIndexed(cmd:String, callback:(Int, SqlRow)->T?):List<T>
    fun <T> query(cmd:String, callback:(SqlRow)->T?):List<T>
    fun queryToMap(cmd:String):ListOfStringMap
    fun queryToList(cmd:String):ListOfStringList
    fun execMutiCommands(cmd:String)
}