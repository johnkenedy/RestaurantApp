package com.example.restaurantapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.ui.activities.ProductsActivity
import com.example.restaurantapp.utils.Constants


class MenuListAdapter(
    private val context: Context,
    private val list: ArrayList<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("MENU", list.toString())
        if (holder is MyViewHolder) {
            holder.itemView.findViewById<TextView>(R.id.tv_category_title).text = list[position]
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductsActivity::class.java)
                intent.putExtra(Constants.CATEGORY, list[position])
                startActivity(context, intent, null)
            }


        }
    }

    override fun getItemCount(): Int = list.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}