package com.example.restaurantapp.ui.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantapp.databinding.ActivityMyOrdersBinding
import com.example.restaurantapp.ui.adapter.MyOrdersListAdapter
import com.example.restaurantapp.ui.firestore.FirestoreClass
import com.example.restaurantapp.ui.models.Order
import com.example.restaurantapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore

class MyOrdersActivity : BaseActivity() {

    private lateinit var binding: ActivityMyOrdersBinding

    private val mFireStore = FirebaseFirestore.getInstance()
    private var searchList: List<Order> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrdersBinding.inflate(layoutInflater)

        binding.etSearchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText: String = binding.etSearchText.text.toString()
                searchInFireStore(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        setContentView(binding.root)
    }

    fun searchInFireStore(searchText: String) {
        mFireStore.collection(Constants.ORDERS).orderBy(Constants.ORDER_NUMBER)
            .startAt(searchText)
            .endAt("$searchText\uf8ff")
            .limit(4)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    searchList = it.result!!.toObjects(Order::class.java)
                    Log.e("Searched Items", searchList.toString())

                    binding.rvMyOrdersItems.layoutManager =
                        LinearLayoutManager(this@MyOrdersActivity, LinearLayoutManager.VERTICAL, false)
                    binding.rvMyOrdersItems.setHasFixedSize(true)
                    binding.rvMyOrdersItems.adapter =  MyOrdersListAdapter(this@MyOrdersActivity, searchList as ArrayList<Order>)


                } else {
                    Log.e("Search Fragment", "Error while searching Products.")
                }
            }
    }

//    fun populateOrdersListOnUI(ordersList: ArrayList<Order>) {
//        hideProgressDialog()
//        if (ordersList.size > 0) {
//            binding.rvMyOrdersItems.visibility = View.VISIBLE
//            binding.tvNoOrdersFound.visibility = View.GONE
//
//            binding.rvMyOrdersItems.layoutManager = LinearLayoutManager(this@MyOrdersActivity)
//            binding.rvMyOrdersItems.setHasFixedSize(true)
//
//            val myOrdersAdapter = MyOrdersListAdapter(this@MyOrdersActivity, ordersList)
//            binding.rvMyOrdersItems.adapter = myOrdersAdapter
//        } else {
//            binding.rvMyOrdersItems.visibility = View.VISIBLE
//            binding.tvNoOrdersFound.visibility = View.GONE
//        }
//    }
//
//    private fun getMyOrdersList() {
//        showProgressDialog()
//        FirestoreClass().getMyOrdersList(this@MyOrdersActivity)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        getMyOrdersList()
//    }

}