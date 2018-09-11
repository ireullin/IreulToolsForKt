package libs.sql.connections

import java.sql.Connection
import java.sql.DriverManager


class JdbcFactory(private val cn: Connection):SqlConnection{

    companion object {
        fun newPostgreSql(host: String, dbname: String, port: String, user: String, password: String)
                = JdbcFactory(DriverManager.getConnection("jdbc:postgresql://$host:$port/$dbname", user, password))

        fun newPostgreSql(host:String, dbname:String, user:String, password:String)
                = newPostgreSql(host, dbname, "5432", user, password)

        fun newPostgreSql(info:Map<String,String>)
                = newPostgreSql(
                    info.getOrDefault("host","unknown_host"),
                    info.getOrDefault("dbname","unknown_dbname"),
                    info.getOrDefault("port","5432"),
                    info.getOrDefault("user","unknown_user"),
                    info.getOrDefault("password","unknown_password")
                )

        fun newSqlite(filename:String)
            = JdbcFactory(DriverManager.getConnection("jdbc:sqlite:$filename"))
    }


    /**
     * callback返回null即停止
     */
    override fun <T> queryIndexed(cmd:String, callback:(Int, SqlRow)->T?):List<T>{
        val buff = arrayListOf<T>()
        var i = 0
        this.cn.prepareStatement(cmd).use { pst->
            ResultSetWrapper(pst.executeQuery()).use { row->
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