package com.doach.mediasearchapp.android.utils

import java.text.SimpleDateFormat
import java.util.*

object ThreadSafeIso8601Util {

    /**
     *
     * 아직 테스트가 완료되지 않음.
     *
     * */

    private const val ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

    private val threadLocalFormat = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue() = SimpleDateFormat(ISO8601_FORMAT, Locale.KOREA).apply {
            timeZone = TimeZone.getTimeZone("Asia/Seoul")
        }
    }

    @Throws(Exception::class)
    fun toMillis(iso8601string: String): Long = threadLocalFormat.get()!!.parse(iso8601string)!!.time

    @Throws(Exception::class)
    fun toIso8601(timeMillis: Long): String = threadLocalFormat.get()!!.format(Date(timeMillis))
}