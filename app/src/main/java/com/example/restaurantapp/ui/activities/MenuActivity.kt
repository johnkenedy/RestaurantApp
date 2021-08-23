package com.example.restaurantapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantapp.databinding.ActivityMenuBinding
import com.example.restaurantapp.ui.adapter.MenuListAdapter
import com.example.restaurantapp.utils.Constants

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private val drinkList: ArrayList<String> =
        arrayListOf(Constants.WATER)

    private val mealList: ArrayList<String> =
        arrayListOf(Constants.APPETIZER, Constants.SIDE_DISH, Constants.MEAT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter()
    }

    private fun adapter() {
        when (intent.extras?.getString(Constants.CATEGORY)) {
            Constants.DRINKS ->
                DrinkMealAdapter(drinkList)
            Constants.MEALS ->
                DrinkMealAdapter(mealList)


        }
    }

    fun DrinkMealAdapter(list: ArrayList<String>) {
        binding.rvMenuCategories.layoutManager = LinearLayoutManager(this@MenuActivity)
        val menuListAdapter = MenuListAdapter(this@MenuActivity, list)
        binding.rvMenuCategories.adapter = menuListAdapter
    }
}