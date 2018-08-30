package libs.datetime

interface Interval{val v:Int }
class Days(override val v:Int):Interval
class Hours(override val v:Int):Interval
class Mins(override val v:Int):Interval
class Secs(override val v:Int):Interval
class Millis(override val v:Int):Interval

fun Int.days() = Days(this)
fun Int.hours() = Hours(this)
fun Int.mins() = Mins(this)
fun Int.secs() = Secs(this)
fun Int.millis() = Millis(this)
