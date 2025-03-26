package com.garde.domain.usecase

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class ExtractExpirationDateUseCase @Inject constructor() {

    private val dateFormats = listOf(
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()),
        SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
        SimpleDateFormat("dd/MM/yy", Locale.getDefault()), // ✅ Support `26/02/25`
        SimpleDateFormat("MM/dd/yy", Locale.getDefault())  // ✅ Support `02/26/25`
    )

    // 🔍 Regex amélioré pour extraire une date même si elle est noyée dans du texte
    private val dateRegex = Regex("""\b(\d{1,2}[/-]\d{1,2}[/-]\d{2,4})\b""")

    operator fun invoke(text: String): String? {
        Log.i("ExtractExpirationDateUseCase", "Processing text: $text")

        // 🔍 Étape 1 : Extraire toutes les dates possibles du texte
        val potentialDates = dateRegex.findAll(text).map { it.value }.toList()
        if (potentialDates.isEmpty()) return null

        Log.i("ExtractExpirationDateUseCase", "Potential dates found: $potentialDates")

        // 🔍 Étape 2 : Vérifier si l'une des dates est valide
        for (dateString in potentialDates) {
            for (format in dateFormats) {
                try {
                    val parsedDate = format.parse(dateString) ?: continue

                    // 🔍 Étape 3 : Normaliser l’année si elle est sur 2 chiffres (`25` → `2025`)
                    val calendar = Calendar.getInstance()
                    calendar.time = parsedDate

                    val year = calendar.get(Calendar.YEAR)
                    if (year < 2000) {
                        calendar.set(Calendar.YEAR, year + 2000)
                    }

                    val formattedDate =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
                    Log.i("ExtractExpirationDateUseCase", "Final formatted date: $formattedDate")

                    return formattedDate // ✅ Retourne la première date valide détectée
                } catch (e: ParseException) {
                    continue
                }
            }
        }

        return null
    }
}
