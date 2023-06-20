package com.meriniguan.polyclinic.utils

import java.util.Calendar

fun getDateString(milliseconds: Long): String {
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milliseconds

    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

    return "$day.6.$year"
}