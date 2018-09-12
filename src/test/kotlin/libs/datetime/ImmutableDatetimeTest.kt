package libs.datetime

import junit.framework.TestCase
import libs.json.Json
import org.junit.Test
import org.slf4j.LoggerFactory


class ImmutableDatetimeTest:TestCase(){
    var log = LoggerFactory.getLogger(ImmutableDatetimeTest::class.java)

    @Test
    fun testSerial() {
        val dt = ImmutableDatetime.of(1980,8,4,13,12,10, 0)
        val stringified = Json.stringify(dt)
        val answer = """{"zone":"Asia/Taipei","time":"1980-08-04 13:12:10.000"}"""
        assertEquals(stringified, answer)
    }

    @Test
    fun testAddorSub() {
        try {
            val pre = ImmutableDatetime.of(2016, 5, 4, 23, 30, 59, 0)
            val cur = pre + 90.days
            log.info(cur.toString())
            assertEquals("2016-08-02 23:30:59", cur.toString("yyyy-MM-dd HH:mm:ss"))
        } catch (e: Exception) {
            log.error("error", e)
            TestCase.assertEquals(false, true)
        }

        try {
            val pre = ImmutableDatetime.of(2016, 5, 4, 23, 30, 59, 0)
            val cur = pre + (48 * 60 * 60 * 1000).millis
            log.info(cur.toString())
            assertEquals("2016-05-06 23:30:59", cur.toString("yyyy-MM-dd HH:mm:ss"))
        } catch (e: Exception) {
            log.error("error", e)
            TestCase.assertEquals(false, true)
        }


        // 閏年
        try {
            val pre = ImmutableDatetime.of(2016, 2, 27, 23, 30, 59, 0)
            val cur = pre + 2.days
            log.info(cur.toString())
            assertEquals("2016-02-29 23:30:59", cur.toString("yyyy-MM-dd HH:mm:ss"))
        } catch (e: Exception) {
            log.error("error", e)
            TestCase.assertEquals(false, true)
        }


        // 沒閏年
        try {
            val pre = ImmutableDatetime.of(2015, 2, 27, 23, 30, 59, 0)
            val cur = pre + 2.days
            log.info(cur.toString())
            assertEquals("2015-03-01 23:30:59", cur.toString("yyyy-MM-dd HH:mm:ss"))
        } catch (e: Exception) {
            log.error("error", e)
            TestCase.assertEquals(false, true)
        }

    }

    @Test
    fun testZeroDay() {
        try {
            val dt1 = ImmutableDatetime.zeroDay()
            log.info(dt1.getTimeZone().toString())
            log.info(dt1.toString())

            val dt2 = ImmutableDatetime.readFrom("1970-01-01 08:00:00.000", "yyyy-MM-dd HH:mm:ss.SSS")
            log.info(dt2.getTimeZone().toString())
            log.info(dt2.toString())
            log.info(dt2.toUTC().toString())

            assertEquals(dt1.equals(dt2), true)
        } catch (e: Exception) {
            log.error("error", e)
            TestCase.assertEquals(false, true)
        }

    }


    @Test
    fun testReset() {
        try {
            val sample = "2016-08-04 13:34:56"
            val dt1 = ImmutableDatetime.readFrom(sample, "yyyy-MM-dd HH:mm:ss")
            log.info(dt1.toString("yyyy-MM-dd HH:mm:ss a"))

            val dt2 = ImmutableDatetime.now()
            log.info(dt2.toString("yyyy-MM-dd HH:mm:ss"))
            dt2.reset(dt1)
            log.info(dt2.toString("yyyy-MM-dd HH:mm:ss a"))
            assertEquals(dt2.toString("yyyy-MM-dd HH:mm:ss"), sample)
        } catch (e: Exception) {
            log.info("error", e)
            TestCase.assertEquals(true, true)
        }

    }

    @Test
    fun testTimeZone() {
        try {
            val dt1 = ImmutableDatetime.now()
            log.info("{} {}", dt1.toString("yyyy-MM-dd HH:mm:ss zzz "), dt1.getTimeZone())

            val dtutc = dt1.toUTC()
            log.info("{} {}", dtutc.toString("yyyy-MM-dd HH:mm:ss zzz "), dtutc.getTimeZone())
            log.info("{} {}", dt1.toString("yyyy-MM-dd HH:mm:ss zzz "), dt1.getTimeZone())

            val dt2 = dt1.toLocalTime()
            log.info("{} {}", dt2.toString("yyyy-MM-dd HH:mm:ss zzz "), dt2.getTimeZone())


            val dt3 = ImmutableDatetime.readFrom("2016-10-25 16:07:21 TST", "yyyy-MM-dd HH:mm:ss zzz")
            log.info("{} {}", dt3.toString("yyyy-MM-dd HH:mm:ss zzz"), dt3.getTimeZone())

            //!!!!!!!!!! it will get CEST. I don't know why ????????
            val dt4 = ImmutableDatetime.readFrom("2016-10-25 16:07:21 UTC", "yyyy-MM-dd HH:mm:ss zzz")
            log.info("{} {}", dt4.toString("yyyy-MM-dd HH:mm:ss zzz"), dt4.getTimeZone())

            val dt5 = ImmutableDatetime.readFrom("2016-10-25 16:07:21", "yyyy-MM-dd HH:mm:ss")
            log.info("{} {}", dt5.toString("yyyy-MM-dd HH:mm:ss zzz"), dt5.getTimeZone())

        } catch (e: Exception) {
            log.error("error", e)
        }

    }

    @Test
    fun testMain() {

        val dt1 = ImmutableDatetime.now()
        val dt2 = dt1.year(2012).month(2).day(15).hour(6).min(10).sec(5).millis(200)
        log.info(dt1.toString())
        log.info(dt2.toString())
        assertEquals(dt1.equals(dt2), false)

        val dt3 = ImmutableDatetime.of(2015, 8, 4, 9, 10, 15, 0)

        log.info(dt3.toString())
        assertEquals("2015-08-04 09:10:15.000", dt3.toString())

        val beginOfDt3 = dt3.toBeginOfDay()
        log.info(beginOfDt3.toString())
        assertEquals("2015-08-04 00:00:00.000", beginOfDt3.toString())

        val endOfDt3 = dt3.toEndOfDay()
        log.info(endOfDt3.toString())
        assertEquals("2015-08-04 23:59:59.999", endOfDt3.toString())

        log.info("{} stamp:{}", dt3.toString(), dt3.stamp())
        assertEquals(1438650615000L, dt3.stamp())

        log.info("==============================")

        val dt4 = ImmutableDatetime.of(dt3.stamp())
        log.info(dt4.toString())
        assertEquals(dt3.stamp(), dt4.stamp())
        log.info("==============================")

        try {
            val dt5 = ImmutableDatetime.readFrom("20140825130972321", "yyyyMMddHHmmssSSS")
            log.info(dt5.toString())
            assertEquals("2014-08-25 13:10:12.321", dt5.toString())
        } catch (e: Exception) {
            log.error("format error", e)
            TestCase.assertEquals(false, true)
        }

        log.info("==============================")

        try {
            val read = ImmutableDatetime.readFrom("2010-03-01 10:00:00", "yyyy-MM-dd HH:mm:ss")
            val dt6 = read - 3.days + 73.hours - 120.mins + 5.secs + 1500.millis
            log.info(dt6.toString())
            assertEquals("2010-03-01 09:00:06.500", dt6.toString())
        } catch (e: Exception) {
            log.error("format error", e)
        }

        log.info("==============================")

        val dtA = ImmutableDatetime.now()
        val dtB = dtA.clone() - 1.days + 2.hours + 3.mins + 5.secs + 600.millis
        //        IDuration du = dtA.during(dtB);
        val duForm = dtB - dtA
        log.info("A is {} ({}), B is {} ({})", dtA.toString(), dtA.stamp(), dtB.toString(), dtB.stamp())
        log.info("duration is {} ({})", duForm.toString(), duForm.stamp())
        log.info("total {} days", duForm.totalDays())
        log.info("total {} hours", duForm.totalHours())
        log.info("total {} minutes", duForm.totalMins())
        log.info("total {} seconds", duForm.totalSecs())
        log.info("total {} milliseconds", duForm.totalMillis())

        val dtC = dtA + Millis(duForm.totalMillis().toInt()) // . addOrSubMillis(duForm.totalMillis())
        assertEquals(dtC.stamp(), dtB.stamp())
    }
}

