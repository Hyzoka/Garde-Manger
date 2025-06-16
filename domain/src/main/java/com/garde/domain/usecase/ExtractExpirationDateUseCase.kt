package com.garde.domain.usecase

import android.util.Log
import com.garde.domain.utils.DateFormatConstants.supportedFormats
import com.garde.domain.utils.FORMAT_DD_MM_YYYY
import com.garde.domain.utils.FORMAT_MM_DD_YYYY
import com.garde.domain.utils.FORMAT_YYYY_MM_DD
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class ExtractExpirationDateUseCase @Inject constructor() {

    // ✅ Regex pour extraire toutes les dates possibles (ex: `26/02/25`, `26022026`)
    private val dateRegex = Regex("""\b(\d{2}[/-]\d{2}[/-]\d{2,4}|\d{8})\b""")

    operator fun invoke(text: String): String? {
        Log.i("ExtractExpirationDateUseCase", "Processing text: $text")

        val potentialDates = dateRegex.findAll(text).map { it.value }.toList()
        if (potentialDates.isEmpty()) return null

        Log.i("ExtractExpirationDateUseCase", "Potential dates found: $potentialDates")

        for (dateString in potentialDates) {
            for (format in supportedFormats.onEach { it.isLenient = false }) {
                try {
                    val parsedDate = format.parse(dateString) ?: continue
                    val calendar = Calendar.getInstance()
                    calendar.time = parsedDate

                    val year = calendar.get(Calendar.YEAR)
                    if (year < 2000) {
                        calendar.set(Calendar.YEAR, year + 2000)
                    }

                    val formattedDate = SimpleDateFormat(FORMAT_DD_MM_YYYY, Locale.getDefault())
                        .format(calendar.time)

                    Log.i("ExtractExpirationDateUseCase", "Final formatted date: $formattedDate")
                    return formattedDate
                } catch (_: ParseException) {
                    continue
                }
            }
        }

        return null
    }
}
