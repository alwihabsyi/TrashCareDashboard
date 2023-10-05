package com.p2mw.trashcaredashboard.model.task

sealed class TaskStatus(val taskStatus: String) {
    data object Complete: TaskStatus("Complete")
    data object Submitted: TaskStatus("Submitted")
    data object Incomplete: TaskStatus("Incomplete")
}