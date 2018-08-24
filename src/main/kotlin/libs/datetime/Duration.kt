package libs.datetime

interface Duration {
    fun dayPart(): Int
    fun hourPart(): Int
    fun minPart(): Int
    fun secPart(): Int
    fun millisPart(): Int

    fun totalDays(): Double
    fun totalHours(): Double
    fun totalMins(): Double
    fun totalSecs(): Double
    fun totalMillis(): Long

    fun stamp(): Long
}
