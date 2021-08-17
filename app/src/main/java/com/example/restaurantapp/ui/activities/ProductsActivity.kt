package com.example.restaurantapp.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantapp.databinding.ActivityProductsBinding
import com.example.restaurantapp.ui.adapter.AppetizerListAdapter
import com.example.restaurantapp.ui.adapter.MeatListAdapter
import com.example.restaurantapp.ui.adapter.SideDishListAdapter
import com.example.restaurantapp.ui.adapter.WaterAndJuiceListAdapter
import com.example.restaurantapp.ui.firestore.FirestoreClass
import com.example.restaurantapp.ui.models.CartItem
import com.example.restaurantapp.ui.models.Items
import com.example.restaurantapp.utils.Constants

class ProductsActivity : BaseActivity() {

    private lateinit var binding: ActivityProductsBinding
    private lateinit var mProductDetails: Items
    private var mProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val message = bundle.getString(Constants.CATEGORY)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        showProductList()
        listeners()

        setContentView(binding.root)
    }

    private fun listeners() {

    }

    fun addToCart() {
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialog()
        FirestoreClass().addCartItems(this@ProductsActivity, cartItem)
    }

    fun addToCartSuccess() {
        hideProgressDialog()
    }

    private fun showProductList() {
        when (intent.extras?.getString(Constants.CATEGORY)) {
            Constants.SIDE_DISH ->
                sideDishAdapter()
            Constants.MEAT ->
                meatAdapter()
            Constants.WATER ->
                waterAndJuicesAdapter()
            Constants.APPETIZER ->
                appetizerAdapter()
        }
    }

    private fun appetizerAdapter() {
        binding.rvProductsList.layoutManager = LinearLayoutManager(this@ProductsActivity)
        val appetizerAdapter = AppetizerListAdapter(this@ProductsActivity)
        binding.rvProductsList.adapter = appetizerAdapter
    }

    private fun sideDishAdapter() {
        binding.rvProductsList.layoutManager = LinearLayoutManager(this@ProductsActivity)
        val sideDishAdapter = SideDishListAdapter(this@ProductsActivity)
        binding.rvProductsList.adapter = sideDishAdapter
    }

    private fun waterAndJuicesAdapter() {
        binding.rvProductsList.layoutManager = LinearLayoutManager(this@ProductsActivity)
        val waterAndJuicesAdapter = WaterAndJuiceListAdapter(this@ProductsActivity)
        binding.rvProductsList.adapter = waterAndJuicesAdapter
    }

    private fun meatAdapter() {
        binding.rvProductsList.layoutManager = LinearLayoutManager(this@ProductsActivity)
        val meatListAdapter = MeatListAdapter(this@ProductsActivity)
        binding.rvProductsList.adapter = meatListAdapter
    }

}