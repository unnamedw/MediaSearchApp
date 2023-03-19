package com.doach.mediasearchapp.android.utils

import java.text.SimpleDateFormat
import java.util.*

object Iso8601Util {

    private const val ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

    private val format get() = SimpleDateFormat(ISO8601_FORMAT, Locale.KOREA).apply {
        timeZone = TimeZone.getTimeZone("Asia/Seoul")

    }

    @Throws(Exception::class)
    fun toMillis(iso8601string: String): Long = format.parse(iso8601string)!!.time

    @Throws(Exception::class)
    fun toIso8601(timeMillis: Long): String = format.format(Date(timeMillis))
}