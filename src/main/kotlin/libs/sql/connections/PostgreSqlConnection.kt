package libs.sql.connections

import java.sql.DriverManager

class PostgreSqlConnection(host:String, dbname:String, port:String, user:String, password:String):SqlConnection{

    constructor(host:String, dbname:String, user:String, password:String)
            :this(host, dbname, "5432", user, password)

    constructor(info:Map<String,String>)
            :this(
            info.getOrDefault("host","unknown_host"),
            info.getOrDefault("dbname","unknown_dbname"),
            info.getOrDefault("port","5432"),
            info.getOrDefault("user","unknown_user"),
            info.getOrDefault("password","unknown_password")
    )

    val url = "jdbc:postgresql://$host:$port/$dbname"
    private val cn = DriverManager.getConnection(url, user, password)

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