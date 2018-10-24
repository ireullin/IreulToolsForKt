package libs.sql.connections

import java.sql.ResultSet

class ResultSetWrapper(val rs: ResultSet):SqlRow{
    override fun next() = this.rs.next()

    /**
     * 從1開始算喔
     */
    override operator fun get(index:Int) = rs.getString(index)

    override operator fun get(columnName:String) = rs.getString(columnName)

    override val resultSet:ResultSet get() = rs

    override val size by lazy { this.rs.metaData.columnCount }

    override fun columnsName() = (1..size).map { rs.metaData.getColumnName(it)}

    override fun toMap() = columnsName().map {name-> name to rs.getString(name) }.toMap()

    override fun toList() = (1..size).map { rs.getString(it) }

    override fun close() { this.rs.close() }
}