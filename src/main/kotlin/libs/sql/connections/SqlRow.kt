package libs.sql.connections

import java.sql.ResultSet

@Deprecated(message = "Deprecated class")
interface SqlRow:AutoCloseable{
    fun next():Boolean
    operator fun get(index:Int):String
    operator fun get(columnName:String):String
    val size:Int
    fun columnsName():List<String>
    fun toMap():Map<String,String>
    fun toList():List<String>
    val resultSet:ResultSet
}