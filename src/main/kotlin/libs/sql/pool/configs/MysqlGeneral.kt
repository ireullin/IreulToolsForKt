package libs.sql.pool.configs

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariConfigMXBean

class MysqlGeneral(val host:String, val port:Int, val dbname:String, val user:String, val pswd:String): HikariConfig(){
    init {
        jdbcUrl = "jdbc:mysql://$host:$port/$dbname"
        username = user
        password = pswd
        addDataSourceProperty( "cachePrepStmts" , "true" );
        addDataSourceProperty( "prepStmtCacheSize" , "250" );
        addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
    }
}