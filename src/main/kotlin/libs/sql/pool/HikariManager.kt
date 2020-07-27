package libs.sql.pool

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import libs.json.toJson
import java.lang.Exception
import java.sql.PreparedStatement


typealias ListOfMap = List<Map<String,String>>
typealias ListOfList = List<List<String>>

class HikariManager private constructor(val hikariDataSource: HikariDataSource){

    private class SqlExectorImp(private val hm:HikariManager):SqlExector{
        private var sql = ""
        private val params = mutableListOf<MutableList<Any>>(mutableListOf())

        override fun statement(sql:String):SqlExector{
            this.sql = sql
            return this
        }

        override fun set(v:Any):SqlExector{
            params.last().add(v)
            return this
        }

        override fun addBatch():SqlExector{
            params.add(mutableListOf())
            return this
        }

        private fun setParams(params:List<Any>, ps: PreparedStatement){
            params.forEachIndexed {i,p ->
                when(p){
                    is Int -> ps.setInt(i+1, p.toInt())
                    is Long -> ps.setLong(i+1, p.toLong())
                    is Double -> ps.setDouble(i+1, p.toDouble())
                    is Float -> ps.setFloat(i+1, p.toFloat())
                    else -> ps.setString(i+1, p.toString())
                }
            }
        }

        override fun <V> queryTo(callback:(row:List<Pair<String,String>>)->V):List<V>{
            hm.connection.use {cn->
                cn.prepareStatement(sql).use {ps->
                    setParams(params.first(), ps)
                    ps.executeQuery().use {rs->
                        val buff = mutableListOf<V>()
                        while (rs.next()){
                            val row = (1..rs.metaData.columnCount).map{ rs.metaData.getColumnName(it) to rs.getString(it) }
                            buff += callback(row)
                        }
                        return buff
                    }
                }
            }
        }

        override fun queryToList():ListOfList = queryTo { row-> row.map{it.second} }
        override fun queryToMap():ListOfMap = queryTo { it.toMap() }

        override fun exec():List<Int>{
            hm.connection.use {cn->
                cn.prepareStatement(sql).use {ps->
                    params.forEach {param->
                        setParams(param, ps)
                        ps.addBatch()
                    }
                    return ps.executeBatch().toList()
                }
            }
        }

    }

    companion object {
        private val pools: MutableMap<String, HikariManager> = mutableMapOf()

        fun newPool(name: String, config: HikariConfig): HikariManager {
            if (pools.containsKey(name)) {
                throw Exception("this name has existed")
            }

            val ds = HikariDataSource(config)
            val hm = HikariManager(ds)
            pools.put(name, hm)
            return pools.get(name)!!
        }

        fun closeAllPools(){
            pools.forEach { _,hm -> hm.hikariDataSource.close() }
        }

        operator fun get(name:String):HikariManager = pools.get(name)!!
    }

    val connection get() = hikariDataSource.connection

    fun create(): SqlExector {
        return SqlExectorImp(this)
    }
}


fun main(args: Array<String>) {
    val testHm = HikariManager.newPool("test", GeneralHikariConfig(DbType.SqlLite("tmps")))

    val created = testHm
            .create()
            .statement("""
                CREATE TABLE tmp_table (
                	name varchar(100),
                   	age varchar(100)
                )
            """.trimIndent())
            .exec()

    println(created.toJson())

    val inserted = HikariManager["test"]
            .create()
            .statement("insert into tmp_table (name, age) values(?,?)")
            .set("fucker").set("20")
            .addBatch()
            .set("mother").set(40)
            .addBatch()
            .set("father").set(50)
            .exec()


    println(inserted.toJson())

    val queried = HikariManager["test"]
            .create()
            .statement("select * from tmp_table where age = ? or age = ?")
            .set(20)
            .set(40)
            .queryToList()

    println(queried.toJson())

    HikariManager.closeAllPools()
}