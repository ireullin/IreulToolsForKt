package libs.datetime

interface Interval{val v:Int }
class Days(override val v:Int):Interval
class Hours(override val v:Int):Interval
class Mins(override val v:Int):Interval
class Secs(override val v:Int):Interval
class Millis(override val v:Int):Interval

inline val Int.days get() = Days(this)
inline val Int.hours get() = Hours(this)
inline val Int.mins get() = Mins(this)
inline val Int.secs get() = Secs(this)
inline val Int.millis get() = Millis(this)
