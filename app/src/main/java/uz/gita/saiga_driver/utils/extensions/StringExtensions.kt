package uz.gita.saiga_driver.utils.extensions

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

// Created by Jamshid Isoqov an 10/12/2022


@SuppressLint("SimpleDateFormat")
fun getCurrentDate(): String {
    val c = Calendar.getInstance().time
    return SimpleDateFormat("MMM dd,yyyy").format(c).uppercase()
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDate(date: Date): String {
    return SimpleDateFormat("MMM dd,yyyy").format(date).uppercase()
}

@SuppressLint("SimpleDateFormat")
fun String.toDate(): Date {
    val format = SimpleDateFormat("MMM dd,yyyy")
    format.parse(this)
    return format.calendar.time
}

fun String.combine(s2: String) = "$this $s2"

@SuppressLint("SimpleDateFormat")
fun String.getTimeWhenFormat(): String {
    val date = this.toDate()
    return SimpleDateFormat("dd-MM-yyyy").format(date).uppercase()
}

fun getCurrentTimeFormat() = getCurrentDate().getTimeWhenFormat()

fun String.getDigitOnly(): Double {
    val builder = StringBuilder()
    for (i in this) {
        if (i.isDigit()) {
            builder.append(i)
        }
    }
    return builder.toString().toDouble()
}

fun String.toTime(): List<String> {
    return this.split(":")
}


