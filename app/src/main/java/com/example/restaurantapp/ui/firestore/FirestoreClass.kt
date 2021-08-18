package com.example.restaurantapp.ui.firestore

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.restaurantapp.ui.activities.MainActivity
import com.example.restaurantapp.ui.activities.ProductsActivity
import com.example.restaurantapp.ui.models.CartItem
import com.example.restaurantapp.ui.models.Order
import com.example.restaurantapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getCurrentUserEmail(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserEmail = ""
        if (currentUser != null) {
            currentUserEmail = currentUser.email.toString()
        }
        return currentUserEmail
    }

    fun addCartItems(activity: ProductsActivity, addToCart: CartItem) {
        mFireStore.collection(Constants.CART_ITEMS)
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating the document for cart item.",
                    e
                )
            }

    }

    fun getCartList(activity: MainActivity) {
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<CartItem> = ArrayList()

                for (i in document.documents) {
                    val cartItem = i.toObject(CartItem::class.java)!!
                    cartItem.id = i.id

                    list.add(cartItem)
                }

                when (activity) {
                    else -> {
                        activity.successCartItemList(list)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    else -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting the cart list items.",
                    e
                )
            }
    }

    fun removeItemFromCart(context: Context, cart_id: String) {
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .delete()
            .addOnSuccessListener {
                when (context) {
                    is MainActivity -> {
                        context.hideProgressDialog()
                    }
                }
            }.addOnFailureListener { e ->
                when (context) {
                    is MainActivity -> {
                        context.hideProgressDialog()
                    }
                }
                Log.e(
                    context.javaClass.simpleName,
                    "Error while removing the item from cart list.",
                    e
                )
            }
    }

    fun updateMyCart(context: Context, cart_id: String, itemHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .update(itemHashMap)
            .addOnSuccessListener {

                when (context) {
                    is MainActivity -> {
                        context.itemUpdateSuccess()
                    }
                }

            }.addOnFailureListener { e ->
                when (context) {
                    is MainActivity -> {
//                        context.hideProgressDialog()
                    }
                }
                Log.e(context.javaClass.simpleName, "Error while updating the cart item.", e)
            }
    }

    fun placeOrder(activity: MainActivity, order: Order) {
        mFireStore.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.orderPlacedSuccessfully()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while placing an order.",
                    e
                )
            }
    }
}