package com.example.restaurantapp.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantapp.databinding.ActivityMyOrdersDetailsBinding
import com.example.restaurantapp.ui.adapter.MyOrdersDetailsAdapter
import com.example.restaurantapp.ui.firestore.FirestoreClass
import com.example.restaurantapp.ui.models.CartItem
import com.example.restaurantapp.ui.models.Order
import com.example.restaurantapp.utils.Constants

class MyOrdersDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityMyOrdersDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrdersDetailsBinding.inflate(layoutInflater)

        var myOrderDetails = Order()
        if (intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)) {
            myOrderDetails = intent.getParcelableExtra(Constants.EXTRA_MY_ORDER_DETAILS)!!
        }
        binding.tvOrderNumberDetailsActivity.text = myOrderDetails.order_number

        setUpUI(myOrderDetails)

        setContentView(binding.root)
    }

    fun setUpUI(orderDetails: Order) {
        binding.rvActivityDetailsOrder.layoutManager =
            LinearLayoutManager(this@MyOrdersDetailsActivity)
        binding.rvActivityDetailsOrder.setHasFixedSize(true)

        val myOrdersDetailsAdapter =
            MyOrdersDetailsAdapter(this@MyOrdersDetailsActivity, orderDetails.items)
        binding.rvActivityDetailsOrder.adapter = myOrdersDetailsAdapter
    }
}