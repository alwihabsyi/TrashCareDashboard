package com.p2mw.trashcaredashboard.model.tacommerce

import android.os.Parcelable
import com.p2mw.trashcaredashboard.model.user.UserAddress
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Parcelize
data class Order(
    val orderStatus: String = "",
    val totalPrice: Double = 0.0,
    val products: List<Cart> = emptyList(),
    val address: UserAddress = UserAddress(),
    val shippingMethod: String = "",
    val paymentPhoto: String = "",
    val date: Date = Date(),
    val orderId: String = Random.nextLong(0, 1_000_000_00).toString() + SimpleDateFormat(
        "yyyyMMdd",
        Locale.ENGLISH
    ).format(Date()),
    val userId: String? = null
) : Parcelable