package com.example.restaurantapp.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val user_id: String = "",
    val product_id: String = "",
    val title: String = "",
    val price: Double = 0.0,
    var cart_quantity: Int = 1,
    var id: String = ""
) : Parcelable
