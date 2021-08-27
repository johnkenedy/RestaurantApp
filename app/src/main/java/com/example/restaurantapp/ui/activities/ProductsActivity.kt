package com.example.restaurantapp.ui.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantapp.databinding.ActivityProductsBinding
import com.example.restaurantapp.ui.adapter.*
import com.example.restaurantapp.ui.firestore.FirestoreClass
import com.example.restaurantapp.ui.models.Items
import com.example.restaurantapp.utils.Constants

class ProductsActivity : BaseActivity() {

    private lateinit var binding: ActivityProductsBinding

    private lateinit var mMeatItemsList: ArrayList<Items>
    private lateinit var mAppetizerItemsList: ArrayList<Items>
    private lateinit var mSideDishItemsList: ArrayList<Items>
    private lateinit var mWaterAndJuiceItemsList: ArrayList<Items>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)

        showProductList()
        listeners()

        setContentView(binding.root)
    }

    private fun listeners() {

    }

    fun addToCartSuccess() {
//        hideProgressDialog()
    }

    private fun showProductList() {
        when (intent.extras?.getString(Constants.CATEGORY)) {
            Constants.SIDE_DISH ->
                FirestoreClass().getSideDishList(this@ProductsActivity)
            Constants.MEAT ->
                FirestoreClass().getMeatList(this@ProductsActivity)
            Constants.WATER ->
                FirestoreClass().getWaterAndJuiceList(this@ProductsActivity)
            Constants.APPETIZER ->
                FirestoreClass().getAppetizerList(this@ProductsActivity)
        }
    }

    fun successMeatItemList(list: ArrayList<Items>) {
        mMeatItemsList = list

        binding.rvProductsList.layoutManager = LinearLayoutManager(this@ProductsActivity)
        val meatListAdapter = MeatListAdapter(this@ProductsActivity, mMeatItemsList)
        binding.rvProductsList.adapter = meatListAdapter

    }

    fun successAppetizerItemList(list: ArrayList<Items>) {
        mAppetizerItemsList = list

        binding.rvProductsList.layoutManager = LinearLayoutManager(this@ProductsActivity)
        val appetizerAdapter = AppetizerListAdapter(this@ProductsActivity, mAppetizerItemsList)
        binding.rvProductsList.adapter = appetizerAdapter
    }

    fun successSideDishItemList(list: ArrayList<Items>) {
        mSideDishItemsList = list

        binding.rvProductsList.layoutManager = LinearLayoutManager(this@ProductsActivity)
        val sideDishAdapter = SideDishListAdapter(this@ProductsActivity, mSideDishItemsList)
        binding.rvProductsList.adapter = sideDishAdapter
    }

    fun successWaterAndJuiceItemList(list: ArrayList<Items>) {
        mWaterAndJuiceItemsList = list

        binding.rvProductsList.layoutManager = LinearLayoutManager(this@ProductsActivity)
        val waterAndJuicesAdapter =
            WaterAndJuiceListAdapter(this@ProductsActivity, mWaterAndJuiceItemsList)
        binding.rvProductsList.adapter = waterAndJuicesAdapter
    }

//    private fun meatAdapter() {
//        binding.rvProductsList.layoutManager = LinearLayoutManager(this@ProductsActivity)
//        val meatListAdapter = MeatListAdapter(this@ProductsActivity)
//        binding.rvProductsList.adapter = meatListAdapter
//    }

}