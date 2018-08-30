package libs.datetime

import com.fasterxml.jackson.annotation.JsonGetter
import java.text.SimpleDateFormat
import java.util.*

fun now() = ImmutableDatetime.now()

class ImmutableDatetime : Datetime {

    private val calendar = GregorianCalendar()

    @JsonGetter("zone")
    override fun getTimeZone(): TimeZone = calendar.timeZone

    private constructor()

    private constructor(tz: TimeZone) {
        calendar.timeZone = tz
    }

    private constructor(dt: Date) {
        calendar.time = dt
    }

    private constructor(dt: Date, tz: TimeZone) {
        calendar.time = dt
        calendar.timeZone = tz
    }

    private constructor(datetime: ReadOnlyDatetime) {
        calendar.timeInMillis = datetime.stamp()
    }

    private constructor(stamp: Long) {
        calendar.timeInMillis = stamp
    }

    private constructor(stamp: Long, tz: TimeZone) {
        calendar.timeInMillis = stamp
        calendar.timeZone = tz
    }

    private constructor(year: Int, month: Int, day: Int, hour: Int, min: Int, sec: Int, millis: Int) {
        calendar.set(year, month - 1, day, hour, min, sec)
        calendar.set(Calendar.MILLISECOND, millis)
    }

    private constructor(year: Int, month: Int, day: Int, hour: Int, min: Int, sec: Int, millis: Int, tz: TimeZone) {
        calendar.set(year, month - 1, day, hour, min, sec)
        calendar.set(Calendar.MILLISECOND, millis)
        calendar.timeZone = tz
    }

    override fun year(): Int {
        return calendar.get(Calendar.YEAR)
    }

    override fun year(v: Int): Datetime {
        return ImmutableDatetime(v, month(), day(), hour(), min(), sec(), millis(), getTimeZone())
    }

    override fun month(): Int {
        return calendar.get(Calendar.MONTH) + 1
    }

    override fun month(v: Int): Datetime {
        return ImmutableDatetime(year(), v, day(), hour(), min(), sec(), millis(), getTimeZone())
    }

    override fun day(): Int {
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    override fun day(v: Int): Datetime {
        return ImmutableDatetime(year(), month(), v, hour(), min(), sec(), millis(), getTimeZone())
    }

    override fun hour(): Int {
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    override fun hour(v: Int): Datetime {
        return ImmutableDatetime(year(), month(), day(), v, min(), sec(), millis(), getTimeZone())
    }

    override fun min(): Int {
        return calendar.get(Calendar.MINUTE)
    }

    override fun min(v: Int): Datetime {
        return ImmutableDatetime(year(), month(), day(), hour(), v, sec(), millis(), getTimeZone())
    }

    override fun sec(): Int {
        return calendar.get(Calendar.SECOND)
    }

    override fun sec(v: Int): Datetime {
        return ImmutableDatetime(year(), month(), day(), hour(), min(), v, millis(), getTimeZone())
    }

    override fun millis(): Int {
        return calendar.get(Calendar.MILLISECOND)
    }

    override fun millis(v: Int): Datetime {
        return ImmutableDatetime(year(), month(), day(), hour(), min(), sec(), v, getTimeZone())
    }

    override fun clone(): Datetime {
        return ImmutableDatetime(stamp(), getTimeZone())
    }

    override fun reset(dt: ReadOnlyDatetime): Datetime {
        throw Exception("This is immutable, you can't reset it.")
    }

    override fun stamp(): Long {
        return calendar.timeInMillis
    }

    override fun toBeginOfDay(): Datetime {
        return ImmutableDatetime(year(), month(), day(), 0, 0, 0, 0, getTimeZone())
    }

    override fun toEndOfDay(): Datetime {
        return ImmutableDatetime(year(), month(), day(), 23, 59, 59, 999, getTimeZone())
    }


    override fun toString(format: String): String {
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = getTimeZone()
        return sdf.format(calendar.time)
    }

    @JsonGetter("time")
    override fun toString(): String = toString("yyyy-MM-dd HH:mm:ss.SSS")

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val datetime = o as ImmutableDatetime?
        return datetime!!.stamp() == this.stamp()
    }

    override fun hashCode(): Int {
        return calendar?.hashCode() ?: 0
    }

    override fun minus(dt: ReadOnlyDatetime) = DurationImp(this.stamp()-dt.stamp())

    override fun minus(interval: Interval): Datetime{
        val dt = ImmutableDatetime(this.stamp(), getTimeZone())
        when(interval){
            is Days -> dt.calendar.add(Calendar.DATE, -interval.v)
            is Hours -> dt.calendar.add(Calendar.HOUR, -interval.v)
            is Mins -> dt.calendar.add(Calendar.MINUTE, -interval.v)
            is Secs -> dt.calendar.add(Calendar.SECOND, -interval.v)
            is Millis -> dt.calendar.add(Calendar.MILLISECOND, -interval.v)
        }
        return dt
    }

    override fun plus(interval: Interval): Datetime{
        val dt = ImmutableDatetime(this.stamp(), getTimeZone())
        when(interval){
            is Days -> dt.calendar.add(Calendar.DATE, interval.v)
            is Hours -> dt.calendar.add(Calendar.HOUR, interval.v)
            is Mins -> dt.calendar.add(Calendar.MINUTE, interval.v)
            is Secs -> dt.calendar.add(Calendar.SECOND, interval.v)
            is Millis -> dt.calendar.add(Calendar.MILLISECOND, interval.v)
        }
        return dt
    }

    override fun toTimeZone(tz: TimeZone): Datetime {
        return ImmutableDatetime(stamp(), tz)
    }

    override fun toUTC(): Datetime {
        val tz = TimeZone.getTimeZone("UTC")
        return ImmutableDatetime(stamp(), tz)
    }

    override fun toLocalTime(): Datetime {
        val tz = TimeZone.getDefault()
        return ImmutableDatetime(stamp(), tz)
    }

    override fun peek(callback: (ReadOnlyDatetime)->Unit): Datetime {
        callback(this)
        return this
    }

    companion object {
        fun readFrom(dtstr: String, format: String): Datetime {
            val sdf = SimpleDateFormat(format)
            val date = sdf.parse(dtstr)
            val tz = sdf.timeZone
            return ImmutableDatetime(date, tz)
        }

        fun now(): Datetime {
            return ImmutableDatetime()
        }

        fun zeroDay(): Datetime {
            val dt = ImmutableDatetime(0)
            return dt.toUTC()
        }

        fun of(tz: TimeZone): Datetime {
            return ImmutableDatetime(tz)
        }

        fun of(dt: Date): Datetime {
            return ImmutableDatetime(dt)
        }

        fun of(dt: Date, tz: TimeZone): Datetime {
            return ImmutableDatetime(dt, tz)
        }

        fun of(datetime: ReadOnlyDatetime): Datetime {
            return ImmutableDatetime(datetime)
        }

        fun of(stamp: Long): Datetime {
            return ImmutableDatetime(stamp)
        }

        fun of(stamp: Long, tz: TimeZone): Datetime {
            return ImmutableDatetime(stamp, tz)
        }

        fun of(year: Int, month: Int, day: Int): Datetime {
            return ImmutableDatetime(year, month, day, 0, 0, 0, 0)
        }

        fun of(year: Int, month: Int, day: Int, tz: TimeZone): Datetime {
            return ImmutableDatetime(year, month, day, 0, 0, 0, 0, tz)
        }

        fun of(year: Int, month: Int, day: Int, hour: Int, min: Int, sec: Int, millis: Int): Datetime {
            return ImmutableDatetime(year, month, day, hour, min, sec, millis)
        }

        fun of(year: Int, month: Int, day: Int, hour: Int, min: Int, sec: Int, millis: Int, tz: TimeZone): Datetime {
            return ImmutableDatetime(year, month, day, hour, min, sec, millis, tz)
        }
    }
}
