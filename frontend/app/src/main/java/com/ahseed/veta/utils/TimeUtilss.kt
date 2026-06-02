package com.ahseed.veta.utils

import android.util.Log
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.time.Instant

fun getTimeAgo(timestamp: String): String {
    return try {

        val trimmedTimestamp =
            timestamp.substringBefore(".") + "." + timestamp.substringAfter(".").take(3)
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        val time = format.parse(trimmedTimestamp)?.time ?: return "Unknown"
        Log.d("TIME:",""+time)

        val now = System.currentTimeMillis()
        val diff = now - time

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        when {
            seconds < 60 -> "Just now"
            minutes < 60 -> "$minutes min ago "
            hours < 24 -> "$hours hour ago"
            days < 7 -> "$days day${if (days > 1) "s" else  ""} ago"
            days < 30 -> "${days / 7} week${if (days / 7 > 1) "" else "s"} ago"
            else -> SimpleDateFormat("yyyyy-MM-dd", Locale.getDefault()).format(Date(time))
        }
    } catch (e: Exception) {
        Log.d("getTimeAgo Error", e.message.toString())
        "Unknown"
    }

}

fun formatDateTime(dateTime:String?):String{
    if(dateTime == null) return ""


    return try {
        val instant = java.time.Instant.parse(dateTime)

        val localDateTime = instant.atZone(
            ZoneId.systemDefault()
        )

        localDateTime.format(
            DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a")
        )
    } catch (e: Exception) {
        dateTime
    }
}

fun formatTime(dateTime: String?): String {
    if (dateTime == null) return "--"

    return try {
        val instant = java.time.Instant.parse(dateTime)
        val localTime = instant.atZone(
            java.time.ZoneId.systemDefault()
        )
        localTime.format(
            DateTimeFormatter.ofPattern("hh:mm a")
        )
    } catch (e: Exception) {
        dateTime
    }
}

fun formatDate(dateTime: String?): String {
    if (dateTime == null) return "--"

    return try {
        val instant = java.time.Instant.parse(dateTime)
        val localTime = instant.atZone(
            java.time.ZoneId.systemDefault()
        )
        localTime.format(
            DateTimeFormatter.ofPattern("ddd MMM yyyy")
        )
    } catch (e: Exception) {
        dateTime
    }
}