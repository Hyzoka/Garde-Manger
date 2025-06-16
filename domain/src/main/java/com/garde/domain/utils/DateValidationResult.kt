package com.garde.domain.utils

sealed class DateValidationResult {
    object Valid : DateValidationResult()
    object InvalidDay : DateValidationResult()
    object InvalidMonth : DateValidationResult()
    object YearTooFar : DateValidationResult()
    object Expired : DateValidationResult()
    object InvalidFormat : DateValidationResult()
}
