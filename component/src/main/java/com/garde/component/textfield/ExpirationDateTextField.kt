package com.garde.component.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
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
            if (input.length <= 8) {
                onDateChange(input)
            }
        },
        visualTransformation = DateTransformation(),
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

class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return dateFilter(text)
    }
}

fun dateFilter(text: AnnotatedString): TransformedText {

    val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
    var out = ""
    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i % 2 == 1 && i < 4) out += "/"
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 1) return offset
            if (offset <= 3) return offset + 1
            if (offset <= 8) return offset + 2
            return 10
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 2) return offset
            if (offset <= 5) return offset - 1
            if (offset <= 10) return offset - 2
            return 8
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}
