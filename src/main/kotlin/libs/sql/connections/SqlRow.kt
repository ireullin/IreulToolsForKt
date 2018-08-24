package libs.sql.connections

interface SqlRow:AutoCloseable{
    fun next():Boolean
    operator fun get(index:Int):String
    operator fun get(columnName:String):String
    val size:Int
    fun columnsName():List<String>
    fun toMap():Map<String,String>
    fun toList():List<String>
}