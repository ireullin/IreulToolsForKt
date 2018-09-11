package libs.sql.connections

import java.sql.DriverManager


class SqlitConnection(filename:String):SqlConnection{

    val url = "jdbc:sqlite:$filename"
    private val cn = DriverManager.getConnection(url)

    /**
     * callback返回null即停止
     */
    override fun <T> queryIndexed(cmd:String, callback:(Int, SqlRow)->T?):List<T>{
        val buff = arrayListOf<T>()
        var i = 0
        this.cn.prepareStatement(cmd).use { pst->
            PostgreSqlRow(pst.executeQuery()).use { row->
                while (row.next()){
                    val rc = callback(i++, row)
                    if(rc==null){
                        break
                    }else{
                        buff.add(rc)
                    }
                }
            }
        }
        return buff
    }

    /**
     * callback返回null即停止
     */
    override fun <T> query(cmd:String, callback:(SqlRow)->T?) = queryIndexed(cmd, { i, row-> callback(row)})

    override fun queryToMap(cmd:String) = query(cmd, {it.toMap()})

    override fun queryToList(cmd:String) = query(cmd, {it.toList()})

    override fun execMutiCommands(cmd:String){
        this.cn.prepareStatement(cmd).use { pst ->
            val rc = pst.executeUpdate()
        }
    }

    override fun close() {
        this.cn.close()
    }
}