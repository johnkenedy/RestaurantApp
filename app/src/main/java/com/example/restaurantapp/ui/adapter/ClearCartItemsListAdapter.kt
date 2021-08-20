package com.example.restaurantapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.ui.activities.MainActivity
import com.example.restaurantapp.ui.firestore.FirestoreClass
import com.example.restaurantapp.ui.models.CartItem
import com.example.restaurantapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore

class ClearCartItemsListAdapter(
    private val context: Context,
    private val list: ArrayList<CartItem>,
    private val updateCartItems: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_order,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            FirebaseFirestore.getInstance()
                .collection(Constants.CART_ITEMS)
                .document(model.id)
                .delete()
        }

        FirestoreClass().getCartList(context as MainActivity)
    }

    override fun getItemCount(): Int = list.size


    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}