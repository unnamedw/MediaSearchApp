package com.doach.mediasearchapp.android

import com.doach.mediasearchapp.android.utils.Iso8601Util
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class Iso8601UtilTest {

    private val testISO8601Str = "2023-04-20T23:59:04.000+09:00"
    private val testTimeMillis = 1682002744000

    @Test
    fun iso8601_to_timeMillis_should_return_true() {
        Assert.assertEquals(testTimeMillis, Iso8601Util.toMillis(testISO8601Str))
    }

    @Test
    fun timeMillis_to_iso8601_should_return_true() {
        Assert.assertEquals(testISO8601Str, Iso8601Util.toIso8601(testTimeMillis))
    }

}