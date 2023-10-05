package com.p2mw.trashcaredashboard.model.tacommerce

sealed class OrderStatus(val orderStatus: String) {
    data object OnGoing: OrderStatus("On Process")
    data object Done: OrderStatus("Done")
    data object Declined: OrderStatus("Declined")
}

fun getOrderStatus(cycleStatus: String): OrderStatus {
    return when (cycleStatus) {
        "On Process" -> { OrderStatus.OnGoing }

        "Declined" -> { OrderStatus.Declined }

        else -> { OrderStatus.Done }
    }
}