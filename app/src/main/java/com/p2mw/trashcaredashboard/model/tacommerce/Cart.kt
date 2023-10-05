package com.p2mw.trashcaredashboard.model.tacommerce

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart (
    val product: Product = Product(),
    val quantity: Int = 0,
): Parcelable