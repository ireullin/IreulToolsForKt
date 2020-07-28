package libs.sql.pool

import com.zaxxer.hikari.HikariConfig

sealed class DbType{
    class Mysql(val host:String, val dbname:String, val user:String, val password:String, val port:Int=3306): DbType(){
        override fun toString() = "jdbc:mysql://$host:$port/$dbname"
    }

    class PostgreSql(val host:String, val dbname:String, val user:String, val password:String, val port:Int=5432): DbType(){
        override fun toString() = "jdbc:postgresql://$host:$port/$dbname"
    }

    class Oracle(val host:String, val dbname:String, val user:String, val password:String, val port:Int=1521): DbType(){
        override fun toString() = "jdbc:oracle:thin:@$host:$port/$dbname"
    }

    class SqlLite(val filename:String): DbType(){
        override fun toString() = "jdbc:sqlite:$filename"
    }

    class SqlLiteInMemory(): DbType(){
        override fun toString() = "jdbc:sqlite::memory"
    }
}



class GeneralHikariConfig(dbType: DbType): HikariConfig(){
    init {
        when(dbType){
            is DbType.Mysql ->{ username=dbType.user; password=dbType.password }
            is DbType.PostgreSql ->{ username=dbType.user; password=dbType.password }
            is DbType.Oracle ->{ username=dbType.user; password=dbType.password }
        }

        jdbcUrl = dbType.toString()

        addDataSourceProperty( "cachePrepStmts" , "true" );
        addDataSourceProperty( "prepStmtCacheSize" , "250" );
        addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
    }
}