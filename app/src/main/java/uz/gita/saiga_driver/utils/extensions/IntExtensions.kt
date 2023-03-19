package uz.gita.saiga_driver.utils.extensions

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

// Created by Jamshid Isoqov on 12/15/2022


fun Int.getTimeFormat(): String {
    val min = this / 60
    val sec = this % 60
    val secString = if (sec < 10) "0$sec" else "$sec"
    return "0$min:$secString"
}

@SuppressLint("SimpleDateFormat")
fun Long.getTimeFormat(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
    return sdf.format(Date(this))
}