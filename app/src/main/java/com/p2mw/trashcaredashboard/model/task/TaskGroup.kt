package com.p2mw.trashcaredashboard.model.task

sealed class TaskGroup(val taskGroup: String) {
    data object DailyImpact1: TaskGroup("Daily Impact 1")
    data object DailyImpact2: TaskGroup("Daily Impact 2")
    data object DailyImpact3: TaskGroup("Daily Impact 3")
}

fun getOrderStatus(cycleStatus: String): TaskGroup {
    return when (cycleStatus) {
        "Daily Impact 1" -> { TaskGroup.DailyImpact1 }

        "Daily Impact 2" -> { TaskGroup.DailyImpact2 }

        else -> { TaskGroup.DailyImpact3 }
    }
}