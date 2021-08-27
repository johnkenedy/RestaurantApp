package com.example.restaurantapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.ui.activities.ProductsActivity
import com.example.restaurantapp.ui.firestore.FirestoreClass
import com.example.restaurantapp.ui.models.CartItem
import com.example.restaurantapp.ui.models.Items
import com.example.restaurantapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore


class AppetizerListAdapter(
    private val context: Context,
    private val list: ArrayList<Items>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_product_list, parent, false)
        )
    }

    @SuppressLint("CutPasteId", "SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("MENU", list.toString())
        val model = list[position]
        val textAndroid = FirestoreClass().getCurrentUserEmail()
        val android = textAndroid.subSequence(0,3).toString()
        if (holder is MyViewHolder) {
            FirebaseFirestore.getInstance().collection(Constants.CART_ITEMS)
                .whereEqualTo(Constants.TITLE, list[position].title)
                .whereEqualTo(Constants.USER_ID, FirestoreClass().getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    if (document.documents.size > 0) {
                        holder.itemView.findViewById<TextView>(R.id.tv_add_product_to_cart)
                            .visibility = View.GONE
                    } else {
                        holder.itemView.findViewById<TextView>(R.id.tv_add_product_to_cart)
                            .visibility = View.VISIBLE
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(
                        "ProductAct ItemsEquals",
                        "Error while checking the existing cart list.",
                        e
                    )
                }

            holder.itemView.findViewById<TextView>(R.id.tv_category_title).text =
                model.title
            holder.itemView.findViewById<TextView>(R.id.tv_add_product_to_cart).text =
                "R$ ${model.price}.00"
            holder.itemView.findViewById<TextView>(R.id.tv_add_product_to_cart)
                .setOnClickListener {
                    val cartItem = CartItem(
                        FirestoreClass().getCurrentUserID(),
                        "",
                        model.title,
                        model.price,
                        Constants.DEFAULT_CART_QUANTITY,
                        android
                    )

                    FirestoreClass().addCartItems(context as ProductsActivity, cartItem)
                    Toast.makeText(context, "PRODUTO ADICIONADO", Toast.LENGTH_SHORT).show()

                    notifyDataSetChanged()
                }
        }
    }

    override fun getItemCount(): Int = list.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}