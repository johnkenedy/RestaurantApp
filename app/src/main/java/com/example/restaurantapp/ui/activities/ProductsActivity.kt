package com.example.restaurantapp.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantapp.databinding.ActivityProductsBinding
import com.example.restaurantapp.ui.models.Constants

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
    }
}