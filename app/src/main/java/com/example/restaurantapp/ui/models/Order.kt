package com.example.restaurantapp.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order (
    val user_id: String = "",
    val items: ArrayList<CartItem> = ArrayList(),
    val order_number: String = "",
    val table: String = "",
    val title: String = "",
    val total_amount: String = "",
    var id: String = "",
    val android: String = ""
) : Parcelable
