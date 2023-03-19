package com.doach.mediasearchapp.android

import com.doach.mediasearchapp.android.utils.Iso8601Util
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class Iso8601UtilTest {

    private val testISO8601Str = "2023-01-25T00:00:01.000+09:00"
    private val testTimeMillis = 1674572401000

    @Test
    fun iso8601_to_timeMillis_should_return_true() {
        Assert.assertTrue(Iso8601Util.toMillis(testISO8601Str) == testTimeMillis)
    }

    @Test
    fun iso8601_to_timeMillis_should_return_false() {
        Assert.assertFalse(Iso8601Util.toMillis(testISO8601Str) == 0L)
    }

    @Test
    fun timeMillis_to_iso8601_should_return_true() {
        Assert.assertTrue(Iso8601Util.toIso8601(testTimeMillis) == testISO8601Str)
    }

    @Test
    fun timeMillis_to_iso8601_should_return_false() {
        Assert.assertFalse(Iso8601Util.toIso8601(0L) == testISO8601Str)
    }

}