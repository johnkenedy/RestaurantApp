package com.example.restaurantapp.ui.firestore

import android.util.Log
import com.example.restaurantapp.ui.activities.ProductsActivity
import com.example.restaurantapp.ui.models.CartItem
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

//    fun getWaterList(activity: Activity) {
//        mFireStore.collection(Constants.WATER)
//            .get()
//            .addOnSuccessListener { document ->
//                Log.e("WATER", document.documents.toString())
//                val itemList: ArrayList<Items> = ArrayList()
//                for (i in document.documents) {
//
//                    val item = i.toObject(Items::class.java)
//                    item!!.product_id = i.id
//                    itemList.add(item)
//                }
//
//                when (activity) {
//                   is MenuActivity ->
//
//               }
//            }
//    }


}