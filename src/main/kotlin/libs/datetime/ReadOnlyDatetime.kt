package libs.datetime

import java.util.*

interface ReadOnlyDatetime {
    fun year(): Int
    fun month(): Int
    fun day(): Int
    fun hour(): Int
    fun min(): Int
    fun sec(): Int
    fun millis(): Int
    fun stamp(): Long
    fun getTimeZone(): TimeZone
    fun toString(format: String): String
    fun peek( callback: (ReadOnlyDatetime)->Unit ): Datetime
    operator fun minus(dt: ReadOnlyDatetime):Duration
}