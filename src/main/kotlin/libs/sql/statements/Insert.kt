package libs.sql.statements

import org.slf4j.LoggerFactory
import java.util.ArrayList

class Insert{
    private val table: String
    private val cols = ArrayList<String>(50)
    private val vals = ArrayList<String>(50)
    private var returning: String? = null

    companion object {
        fun into(table: String)=Insert(table)
    }

    private constructor(table: String){
        this.table = table
    }

    fun put(column: Any, value: Any): Insert {
        cols.add(column.toString())
        vals.add(sqlSyntaxEscape(value.toString()))
        return this
    }

    fun returning(column: Any): Insert {
        returning = column.toString()
        return this
    }

    fun toString(doesIndent: Boolean): String {
        return toString()
    }

    override fun toString(): String {
        val head = StringBuilder(100)
        head.append("insert into ")
        head.append(table)
        head.append(" (")

        val body = StringBuilder(100)
        body.append("values (")

        for (i in cols.indices) {
            head.append(cols[i])

            body.append("'")
            body.append(vals[i])
            body.append("'")

            if (i == cols.size - 1)
                break

            head.append(",")
            body.append(",")
        }

        head.append(") ")
        body.append(")")

        head.append(body)

        if (returning != null) {
            head.append(" returning ").append(returning)
        }

        return head.toString()
    }
}