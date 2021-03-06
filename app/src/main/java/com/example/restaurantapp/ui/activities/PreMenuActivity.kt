package com.example.restaurantapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantapp.databinding.ActivityMenuBinding
import com.example.restaurantapp.ui.adapter.MenuListAdapter
import com.example.restaurantapp.ui.adapter.PreMenuListAdapter

class PreMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter()
    }

    private fun adapter() {
        binding.rvMenuCategories.layoutManager = LinearLayoutManager(this@PreMenuActivity)
        val preMenuListAdapter = PreMenuListAdapter(this@PreMenuActivity)
        binding.rvMenuCategories.adapter = preMenuListAdapter
    }
}