package com.example.restaurantapp.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val user_id: String = "",
    val product_id: String = "",
    val title: String = "",
    val price: Long = 0,
    var cart_quantity: Int = 1,
    var android: String = "",
    var id: String = ""

) : Parcelable
