package libs.datetime

import java.util.*

interface Datetime : ReadOnlyDatetime {

    fun year(v: Int): Datetime
    fun month(v: Int): Datetime
    fun day(v: Int): Datetime
    fun hour(v: Int): Datetime
    fun min(v: Int): Datetime
    fun sec(v: Int): Datetime
    fun millis(v: Int): Datetime

    fun addOrSubDay(v: Int): Datetime
    fun addOrSubHour(v: Int): Datetime
    fun addOrSubMin(v: Int): Datetime
    fun addOrSubSec(v: Int): Datetime
    fun addOrSubMillis(v: Long): Datetime

    /*
    It's the only method which changes inner value.
     */
    fun reset(dt: ReadOnlyDatetime): Datetime

    fun clone(): Datetime
    fun toBeginOfDay(): Datetime
    fun toEndOfDay(): Datetime
    fun toTimeZone(tz: TimeZone): Datetime
    fun toUTC(): Datetime
    fun toLocalTime(): Datetime
}
