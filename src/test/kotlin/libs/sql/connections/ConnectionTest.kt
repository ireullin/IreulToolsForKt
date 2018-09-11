package libs.sql.connections

import junit.framework.TestCase
import libs.sql.statements.Insert
import org.junit.Test

class ConnectionTest: TestCase() {

    @Test
    fun testSqlite() {
        val file = "/tmp/test.sqlite"
        val tbname = "tb1"
        JdbcFactory.newSqlite(file).use { cn ->
            val drop = "DROP TABLE IF EXISTS $tbname"
            cn.execMutiCommands(drop)

            val create = """CREATE TABLE "$tbname" ("id" INTEGER PRIMARY KEY AUTOINCREMENT, "name" TEXT, "age" INTEGER)"""
            cn.execMutiCommands(create)

            (1..10).forEach{
                val insert = Insert.into(tbname).put("name","user_$it").put("age",it+10).toString()
                cn.execMutiCommands(insert)
            }

            val select = "select * from $tbname"
            val result = cn.queryToList(select)
            println(result)
        }



    }
}