package com.example.restaurantapp.ui.firestore

import android.app.Activity
import android.util.Log
import com.example.restaurantapp.ui.activities.MainActivity
import com.example.restaurantapp.ui.activities.MenuActivity
import com.example.restaurantapp.ui.models.Constants
import com.example.restaurantapp.ui.models.Items
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

    fun getWaterList(activity: Activity) {
        mFireStore.collection(Constants.WATER)
            .get()
            .addOnSuccessListener { document ->
                Log.e("WATER", document.documents.toString())
                val itemList: ArrayList<Items> = ArrayList()
                for (i in document.documents) {

                    val item = i.toObject(Items::class.java)
                    item!!.product_id = i.id
                    itemList.add(item)
                }

//                when (activity) {
//                    is MenuActivity ->
//
//                }
            }
    }


}