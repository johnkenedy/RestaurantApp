package com.example.restaurantapp.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.budiyev.android.codescanner.*
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.ActivityMainBinding
import com.example.restaurantapp.ui.adapter.CartItemsListAdapter
import com.example.restaurantapp.ui.firestore.FirestoreClass
import com.example.restaurantapp.ui.models.CartItem
import com.example.restaurantapp.ui.models.Order
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var codeScanner: CodeScanner

    private var mTotalAmount: Double = 0.0
    private lateinit var mCartItemsList: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 123)
        }

        listeners()

        val scannerView: CodeScannerView = binding.scannerView
        codeScanner = CodeScanner(applicationContext, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS

        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.CONTINUOUS
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        setContentView(binding.root)
    }

    private fun listeners() {
        binding.etTableNumber.setOnClickListener {
            startScanningTable()
        }

        binding.etOrderNumber.setOnClickListener {
            startScanningOrder()
        }

        binding.toolbar.setOnClickListener {
            binding.scannerView.visibility = View.GONE
        }

        binding.fabSendOrder.setOnClickListener {

        }

        binding.btnAccessMenu.setOnClickListener {
            startActivity(Intent(applicationContext, MenuActivity::class.java))
        }

    }

    private fun getCartItemsList() {
        FirestoreClass().getCartList(this@MainActivity)
    }

    @SuppressLint("SimpleDateFormat")
    private fun placeAnOrder() {
        validateDetails()
        showProgressDialog()

        val orderNumber = binding.etOrderNumber.text.toString()
        val tableNumber = binding.etTableNumber.text.toString()

        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss")
        val currentDateAndTime: String = simpleDateFormat.format(Date())

        val order = Order(
            FirestoreClass().getCurrentUserID(),
            mCartItemsList,
            orderNumber,
            tableNumber,
            "Comanda: $orderNumber, Mesa: $tableNumber Hora: $currentDateAndTime",
            mTotalAmount.toString(),
            System.currentTimeMillis().toString()
        )

        FirestoreClass().placeOrder(this@MainActivity, order)
    }

    fun successCartItemList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()
        mCartItemsList = cartList

        binding.rvOrdersList.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvOrdersList.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@MainActivity, mCartItemsList, false)
        binding.rvOrdersList.adapter = cartListAdapter

        var total = 0.0
        for (item in mCartItemsList) {
            val price = item.price.toDouble()
            val quantity = item.cart_quantity.toInt()
            total += (price * quantity)
        }
        val roundedTotal = "%.2f".format(total).toDouble()
        binding.tvTotalAmount.text = "R$$roundedTotal"

        Log.e("Cart Items", mCartItemsList.toString())
    }

    private fun validateDetails(): Boolean {

        when {
            binding.etOrderNumber.text.length != 4 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_order_number), true)
            }
            binding.etTableNumber.text.length != 3 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_table_number), true)
            }
            mTotalAmount == 0.0 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_order), true)
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        itemUpdateSuccess()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        binding.scannerView.visibility = View.GONE
        showErrorSnackBar("Clique novamente para sair.", false)
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun startScanningTable() {
        binding.scannerView.visibility = View.VISIBLE


        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val tableNumber = it.text
                binding.etTableNumber.setText(tableNumber)
                binding.scannerView.visibility = View.GONE

            }
        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(
                    this,
                    "Camera initialization error: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        codeScanner.startPreview()
    }

    private fun startScanningOrder() {
        binding.scannerView.visibility = View.VISIBLE

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val tableNumber = it.text
                binding.etOrderNumber.setText(tableNumber)
                binding.scannerView.visibility = View.GONE

            }
        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(
                    this,
                    "Camera initialization error: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        codeScanner.startPreview()
    }

    fun itemUpdateSuccess() {
        getCartItemsList()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
                startScanningTable()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}