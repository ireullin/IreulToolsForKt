package libs.sql.pool

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import libs.sql.pool.configs.MysqlGeneral
import java.lang.Exception
import java.sql.Connection


typealias ListOfMap = List<Map<String,String>>
typealias ListOfList = List<List<String>>

class HikariManager private constructor(val hikariDataSource: HikariDataSource){

     private class SqlExectorInHikariManager(private val cn:Connection):SqlExector{
        private var sql = ""
        private val ps = cn.prepareStatement(sql)
        private var i = 1

        override fun statement(sql:String):SqlExector{
            this.sql = sql
            return this
        }

         override fun <V> set(v:V):SqlExector{
            when(v){
                is Int -> ps.setInt(i++, v)
                is Long -> ps.setLong(i++, v)
                is Float -> ps.setFloat(i++, v)
                is Double -> ps.setDouble(i++,v)
                else -> ps.setString(i++, v.toString())
            }
            return this
        }

         override fun <V> queryTo(callback:(row:List<Pair<String,String>>)->V):List<V>{
            val rs = ps.executeQuery()
            val buff = mutableListOf<V>()
            while (rs.next()){
                val row = (1..rs.metaData.columnCount).map{ rs.metaData.getColumnName(it) to rs.getString(it) }
                buff += callback(row)
            }
            return buff
        }

         override fun queryToList():ListOfList = queryTo { row-> row.map{it.second} }

         override fun queryToMap():ListOfMap = queryTo { it.toMap() }

        override fun close() {
            try{ps.close()}catch (e: Exception){}
            try{cn.close()}catch (e: Exception){}
        }
    }

    companion object {
        private val pools: MutableMap<String, HikariManager> = mutableMapOf()

        fun create(name: String, config: HikariConfig): HikariManager {
            if (pools.containsKey(name)) {
                throw Exception("this name has existed")
            }

            val ds = HikariDataSource(config)
            val hm = HikariManager(ds)
            pools.put(name, hm)
            return pools.get(name)!!
        }

        fun releaseAll(){
            pools.forEach { _,hm -> hm.hikariDataSource.close() }
        }

        operator fun get(name:String):HikariManager = pools.get(name)!!
    }

    val connection get() = hikariDataSource.connection

    fun generateExector(): SqlExector {
        return SqlExectorInHikariManager(connection)
    }
}


fun main(args: Array<String>) {
    HikariManager.create("test", MysqlGeneral("",1234,"","",""))
    val exector = HikariManager["test"].generateExector()
    exector.use {
        it.statement("select * from test where id = ? or id = ?")
                .set(10)
                .set(11)
                .queryToList()
    }



    HikariManager.releaseAll()
}