package libs.sql.statements

fun sqlSyntaxEscape(s: String): String {
    return s.replace("'", "''")
}
