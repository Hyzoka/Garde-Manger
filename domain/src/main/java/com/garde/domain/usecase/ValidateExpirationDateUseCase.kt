package com.garde.domain.usecase

import com.garde.domain.utils.DateFormatConstants
import com.garde.domain.utils.DateValidationResult
import java.util.Calendar
import javax.inject.Inject

class ValidateExpirationDateUseCase @Inject constructor() {

    operator fun invoke(dateString: String): DateValidationResult {
        val now = Calendar.getInstance()
        val maxYear = now.get(Calendar.YEAR) + 10

        for (format in DateFormatConstants.supportedFormats) {
            try {
                val parsedDate = format.parse(dateString) ?: continue

                val cal = Calendar.getInstance().apply { time = parsedDate }
                val day = cal.get(Calendar.DAY_OF_MONTH)
                val month = cal.get(Calendar.MONTH) + 1
                val year = cal.get(Calendar.YEAR)

                if (day !in 1..31) return DateValidationResult.InvalidDay
                if (month !in 1..12) return DateValidationResult.InvalidMonth
                if (year > maxYear) return DateValidationResult.YearTooFar
                if (parsedDate.before(now.time)) return DateValidationResult.Expired

                return DateValidationResult.Valid

            } catch (_: Exception) {
                continue
            }
        }

        return DateValidationResult.InvalidFormat
    }
}

