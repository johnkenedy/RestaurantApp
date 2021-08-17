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
import com.example.restaurantapp.ui.activities.ProductsActivity
import com.example.restaurantapp.ui.firestore.FirestoreClass
import com.example.restaurantapp.ui.models.CartItem
import com.example.restaurantapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore


class AppetizerListAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = arrayOf(
        arrayListOf(Constants.OLIVE, "R$20.00"),
        arrayListOf(Constants.PALM_HEART, "R$20.00"),
        arrayListOf(Constants.SLICED_SALAM, "R$20.00"),
        arrayListOf(Constants.MIXED, "R$25.00"),
        arrayListOf(Constants.FRIED_MEAT_BALLS, "R$30.00")
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
            FirebaseFirestore.getInstance().collection(Constants.CART_ITEMS)
                .whereEqualTo(Constants.PRODUCT_ID, list[position][0])
                .get()
                .addOnSuccessListener { document ->
                    if (document.documents.size > 0) {
                        holder.itemView.findViewById<ImageView>(R.id.iv_add_product_to_cart)
                            .visibility = View.GONE
                    } else {
                        holder.itemView.findViewById<ImageView>(R.id.iv_add_product_to_cart)
                            .visibility = View.VISIBLE
                    }
                }
                .addOnFailureListener { e ->
//                activity.hideProgressDialog()
                    Log.e(
                        "ProductAct ItemsEquals",
                        "Error while checking the existing cart list.",
                        e
                    )
                }

            holder.itemView.findViewById<TextView>(R.id.tv_category_title).text = list[position][0]
            holder.itemView.findViewById<ImageView>(R.id.iv_add_product_to_cart)
                .setOnClickListener {
                    val cartItem = CartItem(
                        FirestoreClass().getCurrentUserID(),
                        list[position][0],
                        list[position][0],
                        list[position][1],
                        Constants.DEFAULT_CART_QUANTITY
                    )

                    FirestoreClass().addCartItems(context as ProductsActivity, cartItem)
                    notifyDataSetChanged()
                }
        }
    }

    override fun getItemCount(): Int = list.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}