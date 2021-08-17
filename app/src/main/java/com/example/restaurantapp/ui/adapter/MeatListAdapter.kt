package com.example.restaurantapp.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.utils.Constants


class MeatListAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = arrayOf(
        arrayListOf(Constants.RUMP_STEAK, "R$95.00"),
        arrayListOf(Constants.TENDERLOIN, "R$85.00"),
        arrayListOf(Constants.CHICKEN, "R$65.00"),
        arrayListOf(Constants.TILAPIA, "R$75.00"),
        arrayListOf(Constants.FRIES, "R$45.00")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_product_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("MENU", list.toString())
        if (holder is MyViewHolder) {
            holder.itemView.findViewById<TextView>(R.id.tv_category_title).text = list[position][0]
            holder.itemView.findViewById<ImageView>(R.id.iv_add_product_to_cart).setOnClickListener {
                Toast.makeText(context, "${list[position][0]}, ${list[position][1]} ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = list.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}