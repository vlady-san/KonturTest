package com.example.konturtest

import android.os.Build
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class DateUtils {
    companion object {
        private val serverDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        private val prettyDateFormat = SimpleDateFormat("dd.MM.yyyy")
        fun formPrettyDate(dateStr: String): String {
            val date = serverDateFormat.parse(dateStr)
            return prettyDateFormat.format(date)
        }
    }
}