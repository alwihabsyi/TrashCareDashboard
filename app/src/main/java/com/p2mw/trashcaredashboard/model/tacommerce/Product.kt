package com.p2mw.trashcaredashboard.model.tacommerce

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val description: String = "",
    val photoUrl: String = ""
): Parcelable
