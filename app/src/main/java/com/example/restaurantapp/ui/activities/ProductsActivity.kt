package com.example.restaurantapp.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantapp.databinding.ActivityProductsBinding
import com.example.restaurantapp.ui.adapter.AppetizerListAdapter
import com.example.restaurantapp.ui.adapter.MeatListAdapter
import com.example.restaurantapp.ui.adapter.SideDishListAdapter
import com.example.restaurantapp.ui.adapter.WaterAndJuiceListAdapter
import com.example.restaurantapp.utils.Constants

class ProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val message = bundle.getString(Constants.CATEGORY)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        showProductList()

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