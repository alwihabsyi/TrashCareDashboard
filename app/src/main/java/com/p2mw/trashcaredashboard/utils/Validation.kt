package com.p2mw.trashcaredashboard.utils

sealed class Validation {
    data object Success: Validation()
    data class Failed(val message: String): Validation()
}

data class FieldsState(
    val email: Validation,
    val password: Validation
)