package libs.datetime

class DurationImp(private val stamp: Long) : Duration {
    private var partial: Partial? = null

    private inner class Partial(stamp: Long) {
        val days: Int
        val hours: Int
        val minutes: Int
        val seconds: Int
        val milliseconds: Int

        init {
            var stamp = stamp
            stamp = Math.abs(stamp)
            this.days = (stamp / (24 * 60 * 60 * 1000)).toInt()
            val dayRem = (stamp % (24 * 60 * 60 * 1000)).toInt()
            this.hours = dayRem / (60 * 60 * 1000)
            val hoursRem = dayRem % (60 * 60 * 1000)
            this.minutes = hoursRem / (60 * 1000)
            val minRem = hoursRem % (60 * 1000)
            this.seconds = minRem / 1000
            this.milliseconds = minRem % 1000
        }
    }

    override fun dayPart(): Int {
        if (partial == null) partial = Partial(stamp)
        return partial!!.days
    }

    override fun hourPart(): Int {
        if (partial == null) partial = Partial(stamp)
        return partial!!.hours
    }

    override fun minPart(): Int {
        if (partial == null) partial = Partial(stamp)
        return partial!!.minutes
    }

    override fun secPart(): Int {
        if (partial == null) partial = Partial(stamp)
        return partial!!.seconds
    }

    override fun millisPart(): Int {
        if (partial == null) partial = Partial(stamp)
        return partial!!.milliseconds
    }

    override fun totalDays(): Double {
        return totalHours() / 24.0
    }

    override fun totalHours(): Double {
        return totalMins() / 60.0
    }

    override fun totalMins(): Double {
        return totalSecs() / 60.0
    }

    override fun totalSecs(): Double {
        return totalMillis() / 1000.0
    }

    override fun totalMillis(): Long {
        return this.stamp
    }

    override fun stamp(): Long {
        return this.stamp
    }

    override fun toString(): String {
        return String.format("%d days, %d hours, %d minutes, %d seconds and %d milliseconds",
                dayPart(),
                hourPart(),
                minPart(),
                secPart(),
                millisPart()
        )
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val duration = o as Duration?
        return duration!!.stamp() == this.stamp()
    }

    override fun hashCode(): Int {
        var result = (stamp xor stamp.ushr(32)).toInt()
        result = 31 * result + if (partial != null) partial!!.hashCode() else 0
        return result
    }
}