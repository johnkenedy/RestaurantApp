package com.example.restaurantapp.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantapp.databinding.ActivityMyOrdersBinding
import com.example.restaurantapp.ui.adapter.MyOrdersListAdapter
import com.example.restaurantapp.ui.firestore.FirestoreClass
import com.example.restaurantapp.ui.models.Order

class MyOrdersActivity : BaseActivity() {

    private lateinit var binding: ActivityMyOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun populateOrdersListOnUI(ordersList: ArrayList<Order>) {
        hideProgressDialog()
        if (ordersList.size > 0) {
            binding.rvMyOrdersItems.visibility = View.VISIBLE
            binding.tvNoOrdersFound.visibility = View.GONE

            binding.rvMyOrdersItems.layoutManager = LinearLayoutManager(this@MyOrdersActivity)
            binding.rvMyOrdersItems.setHasFixedSize(true)

            val myOrdersAdapter = MyOrdersListAdapter(this@MyOrdersActivity, ordersList)
            binding.rvMyOrdersItems.adapter = myOrdersAdapter
        } else {
            binding.rvMyOrdersItems.visibility = View.VISIBLE
            binding.tvNoOrdersFound.visibility = View.GONE
        }
    }

    private fun getMyOrdersList() {
        showProgressDialog()
        FirestoreClass().getMyOrdersList(this@MyOrdersActivity)
    }

    override fun onResume() {
        super.onResume()
        getMyOrdersList()
    }

}