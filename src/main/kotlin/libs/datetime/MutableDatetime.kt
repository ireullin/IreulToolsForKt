package libs.datetime

import java.text.SimpleDateFormat
import java.util.*

class MutableDatetime : Datetime {
    private var immutableDatetime: Datetime

    override fun getTimeZone(): TimeZone = this.immutableDatetime.getTimeZone()


    private constructor() {
        this.immutableDatetime = ImmutableDatetime.now()
    }

    private constructor(tz: TimeZone) {
        this.immutableDatetime = ImmutableDatetime.of(tz)
    }

    private constructor(dt: Date) {
        this.immutableDatetime = ImmutableDatetime.of(dt)
    }

    private constructor(dt: Date, tz: TimeZone) {
        this.immutableDatetime = ImmutableDatetime.of(dt, tz)
    }

    private constructor(datetime: ReadOnlyDatetime) {
        this.immutableDatetime = ImmutableDatetime.of(datetime)
    }

    private constructor(stamp: Long) {
        this.immutableDatetime = ImmutableDatetime.of(stamp)
    }

    private constructor(stamp: Long, tz: TimeZone) {
        this.immutableDatetime = ImmutableDatetime.of(stamp, tz)
    }

    private constructor(year: Int, month: Int, day: Int, hour: Int, min: Int, sec: Int, millis: Int) {
        this.immutableDatetime = ImmutableDatetime.of(year, month, day, hour, min, sec, millis)
    }

    private constructor(year: Int, month: Int, day: Int, hour: Int, min: Int, sec: Int, millis: Int, tz: TimeZone) {
        this.immutableDatetime = ImmutableDatetime.of(year, month, day, hour, min, sec, millis, tz)
    }

    override fun year(v: Int): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.year(v)
        return this
    }

    override fun month(v: Int): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.month(v)
        return this
    }

    override fun day(v: Int): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.day(v)
        return this
    }

    override fun hour(v: Int): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.hour(v)
        return this
    }

    override fun min(v: Int): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.min(v)
        return this
    }

    override fun sec(v: Int): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.sec(v)
        return this
    }

    override fun millis(v: Int): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.millis(v)
        return this
    }

    override fun addOrSubDay(v: Int): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.addOrSubDay(v)
        return this
    }

    override fun addOrSubHour(v: Int): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.addOrSubHour(v)
        return this
    }

    override fun addOrSubMin(v: Int): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.addOrSubMin(v)
        return this
    }

    override fun addOrSubSec(v: Int): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.addOrSubSec(v)
        return this
    }

    override fun addOrSubMillis(v: Long): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.addOrSubMillis(v)
        return this
    }

    override fun reset(dt: ReadOnlyDatetime): Datetime {
        this.immutableDatetime = ImmutableDatetime.of(dt.stamp(), dt.getTimeZone())
        return this
    }

    override fun clone(): Datetime {
        return MutableDatetime.of(this.immutableDatetime)
    }

    override fun toBeginOfDay(): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.toBeginOfDay()
        return this
    }

    override fun toEndOfDay(): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.toEndOfDay()
        return this
    }

    override fun toTimeZone(tz: TimeZone): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.toTimeZone(tz)
        return this
    }

    override fun toUTC(): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.toUTC()
        return this
    }

    override fun toLocalTime(): Datetime {
        this.immutableDatetime = this.immutableDatetime!!.toLocalTime()
        return this
    }

    override fun minus(dt: ReadOnlyDatetime) = this.immutableDatetime!!.minus(dt)

//    override fun to(dt: ReadOnlyDatetime): Duration {
//        return this.immutableDatetime!!.to(dt)
//    }
//    override fun from(dt: ReadOnlyDatetime): Duration {
//        return this.immutableDatetime!!.from(dt)
//    }

    override fun year(): Int {
        return this.immutableDatetime!!.year()
    }

    override fun month(): Int {
        return this.immutableDatetime!!.month()
    }

    override fun day(): Int {
        return this.immutableDatetime!!.day()
    }

    override fun hour(): Int {
        return this.immutableDatetime!!.hour()
    }

    override fun min(): Int {
        return this.immutableDatetime!!.min()
    }

    override fun sec(): Int {
        return this.immutableDatetime!!.sec()
    }

    override fun millis(): Int {
        return this.immutableDatetime.millis()
    }

    override fun stamp(): Long {
        return this.immutableDatetime.stamp()
    }

    override fun toString(): String {
        return this.immutableDatetime.toString()
    }

    override fun toString(format: String): String {
        return this.immutableDatetime.toString(format)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val datetime = o as MutableDatetime?
        return datetime?.stamp() == this.stamp()
    }

    override fun peek(callback: (ReadOnlyDatetime) -> Unit): Datetime {
        callback(this)
        return this
    }

    companion object {
        fun readFrom(dtstr: String, format: String): Datetime {
            val sdf = SimpleDateFormat(format)
            val date = sdf.parse(dtstr)
            val tz = sdf.timeZone
            return MutableDatetime(date, tz)
        }

        fun now(): Datetime {
            return MutableDatetime()
        }

        fun zeroDay(): Datetime {
            val dt = MutableDatetime(0)
            return dt.toUTC()
        }

        fun of(tz: TimeZone): Datetime {
            return MutableDatetime(tz)
        }

        fun of(dt: Date): Datetime {
            return MutableDatetime(dt)
        }

        fun of(dt: Date, tz: TimeZone): Datetime {
            return MutableDatetime(dt, tz)
        }

        fun of(datetime: ReadOnlyDatetime): Datetime {
            return MutableDatetime(datetime)
        }

        fun of(stamp: Long): Datetime {
            return MutableDatetime(stamp)
        }

        fun of(stamp: Long, tz: TimeZone): Datetime {
            return MutableDatetime(stamp, tz)
        }

        fun of(year: Int, month: Int, day: Int): Datetime {
            return MutableDatetime(year, month, day, 0, 0, 0, 0)
        }

        fun of(year: Int, month: Int, day: Int, tz: TimeZone): Datetime {
            return MutableDatetime(year, month, day, 0, 0, 0, 0, tz)
        }

        fun of(year: Int, month: Int, day: Int, hour: Int, min: Int, sec: Int, millis: Int): Datetime {
            return MutableDatetime(year, month, day, hour, min, sec, millis)
        }

        fun of(year: Int, month: Int, day: Int, hour: Int, min: Int, sec: Int, millis: Int, tz: TimeZone): Datetime {
            return MutableDatetime(year, month, day, hour, min, sec, millis, tz)
        }
    }
}