package libs.sql.pool

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import libs.json.toJson
import java.lang.Exception
import java.sql.PreparedStatement
import java.sql.Statement


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

        override fun setFromList(v:List<Any>):SqlExector{
            params.last().addAll(v)
            return this
        }

        override fun setFromVararg(vararg v:Any):SqlExector{
            params.last().addAll(v.toList())
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

        override fun exec(statementOption:Int):List<Int>{
            hm.connection.use {cn->
                cn.prepareStatement(sql, statementOption).use { ps->
                    params.forEach {param->
                        setParams(param, ps)
                        ps.addBatch()
                    }
                    return ps.executeBatch().toList()
                }
            }
        }

        override fun exec() = exec(Statement.RETURN_GENERATED_KEYS)


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

        fun newPool(name:String, dbType: DbType): HikariManager {
            val conf = HikariConfig()
            conf.addDataSourceProperty( "cachePrepStmts" , "true" );
            conf.addDataSourceProperty( "prepStmtCacheSize" , "250" );
            conf.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
            conf.jdbcUrl = dbType.toString()
            when(dbType){
                is DbType.Mysql ->{ conf.username=dbType.user; conf.password=dbType.password }
                is DbType.PostgreSql ->{ conf.username=dbType.user; conf.password=dbType.password }
                is DbType.Oracle ->{ conf.username=dbType.user; conf.password=dbType.password }
            }
            return newPool(name, conf)
        }

        fun closeAllPools(){
            pools.forEach { _,hm -> hm.hikariDataSource.close() }
        }

        operator fun get(name:String):HikariManager = pools.get(name)!!
    }

    val connection get() = hikariDataSource.connection

    fun create():SqlExector=SqlExectorImp(this)
}


fun main(args: Array<String>) {
//    val testHm = HikariManager.newPool("test", GeneralHikariConfig(DbType.SqlLite("tmps")))
    val testHm = HikariManager.newPool("test", DbType.PostgreSql("192.168.1.252","AssociationRule","postgres","0933726835"))

//    val created = testHm
//            .create()
//            .statement("""
//                CREATE TABLE tmp_table (
//                	name varchar(100),
//                   	age varchar(100)
//                )
//            """.trimIndent())
//            .exec()
//
//    println(created.toJson())

//    val now = ImmutableDatetime.now().toString()
//    val inserted = HikariManager["test"]
//            .create()
//            .statement("insert into tmp_table (name, age, created_at) values(?,?,?)")
//            .set("fucker").set("20").set(now)
//            .addBatch()
//            .set("mother").set(40).set(now)
//            .addBatch()
//            .set("father").set(50).set(now)
//            .exec()


//    println(inserted.toJson())

    while(true) {
        val buyers = HikariManager["test"]
                .create()
                .statement("select distinct buyer_id from public.association_rule_orders")
                .queryTo { it.first().second }

        println(buyers.toJson())

        buyers.forEach{
            val queried = HikariManager["test"]
                    .create()
                    .statement("select * from public.association_rule_orders where buyer_id = ?")
                    .set(it)
                    .queryToMap()

            println(queried.toJson())
        }

        println("waiting")
        Thread.sleep(120*1000)
    }

    HikariManager.closeAllPools()
}