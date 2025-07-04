package com.garde.domain.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


const val FORMAT_DD_MM_YYYY = "dd/MM/yyyy"
const val FORMAT_MM_DD_YYYY = "MM/dd/yyyy"
const val FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
const val FORMAT_DD_MM_YY = "dd/MM/yy"
const val FORMAT_MM_DD_YY = "MM/dd/yy"
const val FORMAT_DDMMYYYY = "ddMMyyyy"

object DateFormatConstants {
    val supportedFormats = listOf(
        SimpleDateFormat(FORMAT_DDMMYYYY, Locale.getDefault()),
        SimpleDateFormat(FORMAT_DD_MM_YYYY, Locale.getDefault()),
        SimpleDateFormat(FORMAT_MM_DD_YYYY, Locale.getDefault()),
        SimpleDateFormat(FORMAT_YYYY_MM_DD, Locale.getDefault()),
        SimpleDateFormat(FORMAT_DD_MM_YY, Locale.getDefault()),
        SimpleDateFormat(FORMAT_MM_DD_YY, Locale.getDefault())
    )
}


fun getRemainingDays(
    expirationDate: String?,
    currentDate: Date = Date(),
    format: String = FORMAT_DD_MM_YYYY
): Int {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    val daysLeft = sdf.parse(expirationDate ?: "") ?: Date()

    val diffInMillis = daysLeft.time - currentDate.time
    return (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
}

