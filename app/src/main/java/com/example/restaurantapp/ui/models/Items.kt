package com.example.restaurantapp.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Items(
    val title: String = "",
    val price: Long = 0,
    val available: String = "",
    var product_id: String = ""
) : Parcelable
