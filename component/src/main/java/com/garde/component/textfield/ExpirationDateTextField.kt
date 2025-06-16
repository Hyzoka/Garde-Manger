package com.garde.component.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.garde.core.R
import com.garde.domain.utils.FORMAT_DD_MM_YYYY

@Composable
fun ExpirationDateTextField(
    dateInput: String,
    onDateChange: (String) -> Unit,
    showError: Int?
) {
    OutlinedTextField(
        value = dateInput,
        onValueChange = { input ->
            val digits = input.filter { it.isDigit() }.take(8)

            val formatted = buildString {
                for (i in digits.indices) {
                    append(digits[i])
                    if ((i == 1 || i == 3) && i != digits.lastIndex) {
                        append('/')
                    }
                }
            }

            onDateChange(formatted)
        },
        label = { Text(stringResource(R.string.enter_expiration_date)) },
        placeholder = { Text(FORMAT_DD_MM_YYYY) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        isError = showError != null,
        supportingText = {
            showError?.let {
                Text(
                    stringResource(showError),
                    color = Color.Red
                )

            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
