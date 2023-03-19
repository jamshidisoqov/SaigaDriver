package uz.gita.saiga_driver.utils.extensions

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

// Created by Jamshid Isoqov an 10/12/2022
@SuppressLint("SimpleDateFormat")
fun getCurrentDate(): String {
    val c = Calendar.getInstance().time
    return SimpleDateFormat("MMM dd,yyyy").format(c)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDate(date: Date): String {
    return SimpleDateFormat("MMM dd,yyyy").format(date)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentTime(date: Date): String {
    return SimpleDateFormat("HH:mm").format(date)
}


@SuppressLint("SimpleDateFormat")
fun String.toDate(): Date {
    val format = SimpleDateFormat("MMM dd,yyyy")
    format.parse(this)
    return format.calendar.time
}

fun String?.combine(s2: String) = "$this $s2"

@SuppressLint("SimpleDateFormat")
fun String.getTimeWhenFormat(): String {
    val date = this.toDate()
    return SimpleDateFormat("dd-MM-yyyy").format(date)
}

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

@SuppressLint("SimpleDateFormat")
fun String.getBackendTimeFormat(): String {
    val date = SimpleDateFormat("dd MMM yyyy HH:mm").parse(this)!!
    return SimpleDateFormat("dd MMM yyyy").format(date)
}

fun String.getFormat(count: Int): String {
    val index = this.indexOf('.')
    val form = if (index > 0) this.substring(0, index) else this
    val builder = StringBuilder(form.reversed())
    val formatterString = StringBuilder()
    builder.chunked(count).forEach { formatterString.append(it.plus(' ')) }
    return formatterString.reversed().toString().trim(' ')
}


