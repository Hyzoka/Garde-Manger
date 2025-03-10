package com.garde.presentation.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    private val dateFormats = listOf(
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()),
        SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    )

    fun extractDate(text: String): String? {
        val regex = Regex("""\b(\d{2}[/-]\d{2}[/-]\d{4}|\d{4}-\d{2}-\d{2})\b""")
        val match = regex.find(text)?.value ?: return null

        for (format in dateFormats) {
            try {
                format.parse(match)?.let {
                    return match // Retourne la date si le format est valide
                }
            } catch (_: Exception) { }
        }
        return null
    }
}
