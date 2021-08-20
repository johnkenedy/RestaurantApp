package com.example.restaurantapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.ui.models.CartItem

class MyOrdersDetailsAdapter(
    private val context: Context,
    private var list: ArrayList<CartItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_order_details, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.findViewById<TextView>(R.id.tv_order_details_product_name).text =
            model.title
        holder.itemView.findViewById<TextView>(R.id.tv_order_details_product_amount).text =
            model.cart_quantity.toString()

    }

    override fun getItemCount(): Int = list.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}