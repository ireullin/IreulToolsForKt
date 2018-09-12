package libs.datetime

interface Interval{val v:Int }
class Days(override val v:Int):Interval
class Hours(override val v:Int):Interval
class Mins(override val v:Int):Interval
class Secs(override val v:Int):Interval
class Millis(override val v:Int):Interval

val Int.days get() = Days(this)
val Int.hours get() = Hours(this)
val Int.mins get() = Mins(this)
val Int.secs get() = Secs(this)
val Int.millis get() = Millis(this)
