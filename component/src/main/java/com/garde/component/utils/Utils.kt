package com.garde.component.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.garde.core.R
import kotlin.math.abs

@Composable
fun getLocalizedExpirationMessage(daysLeft: Int): String {
    return when {
        daysLeft < 0 -> stringResource(R.string.expired_since_days, abs(daysLeft))
        daysLeft == 0 -> stringResource(R.string.expire_today)
        daysLeft < 365 -> stringResource(R.string.expire_in_days, daysLeft)
        else -> {
            val years = daysLeft / 365
            val remainingDays = daysLeft % 365
            if (remainingDays == 0)
                stringResource(R.string.expire_in_years, years)
            else
                stringResource(R.string.expire_in_years_and_days, years, remainingDays)
        }
    }
}
