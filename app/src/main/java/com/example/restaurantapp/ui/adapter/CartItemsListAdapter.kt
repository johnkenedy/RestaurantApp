package com.example.restaurantapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.ui.activities.MainActivity
import com.example.restaurantapp.ui.firestore.FirestoreClass
import com.example.restaurantapp.ui.models.CartItem
import com.example.restaurantapp.utils.Constants

class CartItemsListAdapter(
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

            holder.itemView.findViewById<TextView>(R.id.tv_order_product_name).text =
                model.title
//            holder.itemView.findViewById<TextView>(R.id.tv_order_product_price).text =
//                "$${model.price}"
            holder.itemView.findViewById<TextView>(R.id.tv_order_product_amount).text =
                model.cart_quantity.toString()

            holder.itemView.findViewById<ImageView>(R.id.iv_order_remove_item)
                .setOnClickListener {
                    when (context) {
                        is MainActivity -> {
                            context.showProgressDialog()
                        }
                    }
                    FirestoreClass().removeItemFromCart(context, model.id)
                }

            holder.itemView.findViewById<ImageView>(R.id.iv_order_remove_item)
                .setOnClickListener {
                    if (model.cart_quantity == 1) {
                        FirestoreClass().removeItemFromCart(context, model.id)
                    } else {
                        val cartQuantity: Int = model.cart_quantity
                        val itemHashMap = HashMap<String, Any>()

                        itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                        FirestoreClass().updateMyCart(context, model.id, itemHashMap)
                    }
                }

            holder.itemView.findViewById<ImageView>(R.id.iv_order_add_item).setOnClickListener {
                val cartQuantity: Int = model.cart_quantity
                val itemHashMap = HashMap<String, Any>()

                itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                FirestoreClass().updateMyCart(context, model.id, itemHashMap)


            }
        }
    }

    override fun getItemCount(): Int = list.size


    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}