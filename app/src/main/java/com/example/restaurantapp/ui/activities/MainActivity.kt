package com.example.restaurantapp.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import com.budiyev.android.codescanner.*
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.ActivityMainBinding
import com.example.restaurantapp.ui.adapter.CartItemsListAdapter
import com.example.restaurantapp.ui.adapter.ClearCartItemsListAdapter
import com.example.restaurantapp.ui.firestore.FirestoreClass
import com.example.restaurantapp.ui.models.CartItem
import com.example.restaurantapp.ui.models.Order
import com.google.firebase.auth.FirebaseAuth
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
//            binding.etOrderNumber.hideKeyboard()
            startScanningOrder()
        }

        binding.toolbar.setOnClickListener {
            binding.scannerView.visibility = View.GONE
        }

        binding.fabSendOrder.setOnClickListener {
            if (validateDetails()) {
                placeAnOrder()
            } else {
                false
            }
        }

        binding.btnAccessMenu.setOnClickListener {
            startActivity(Intent(applicationContext, PreMenuActivity::class.java))
        }

        binding.btnAccessHistoric.setOnClickListener {
            startActivity(Intent(this, MyOrdersActivity::class.java))
        }

        binding.ivLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.fabClearAll.setOnClickListener {
            clearList()
        }

    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun getCartItemsList() {
        FirestoreClass().getCartList(this@MainActivity)
    }

    @SuppressLint("SimpleDateFormat")
    private fun placeAnOrder() {
        showProgressDialog()
        val orderNumber = binding.etOrderNumber.text.toString()
        val tableNumber = binding.etTableNumber.text.toString()

        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        val textAndroid = FirestoreClass().getCurrentUserEmail()
        val android = textAndroid.subSequence(0, 3).toString()
        val order = Order(
            FirestoreClass().getCurrentUserID(),
            mCartItemsList,
            orderNumber,
            tableNumber,
            "Comanda: $orderNumber, Mesa: $tableNumber Hora: $currentDateAndTime",
            binding.tvTotalAmount.text.toString(),
            System.currentTimeMillis().toString(),
            android
        )

        FirestoreClass().placeOrder(this@MainActivity, order)
    }

    fun orderPlacedSuccessfully() {
        Toast.makeText(this, "Pedido enviado!", Toast.LENGTH_SHORT).show()
        hideProgressDialog()
    }

    fun successCartItemList(cartList: ArrayList<CartItem>) {
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

    private fun clearList() {
        if (mCartItemsList.isNotEmpty()) {
            val clearCartListAdapter = ClearCartItemsListAdapter(this@MainActivity, mCartItemsList, false)
            binding.rvOrdersList.adapter = clearCartListAdapter
        } else {
            val cartListAdapter = CartItemsListAdapter(this@MainActivity, mCartItemsList, false)
            binding.rvOrdersList.adapter = cartListAdapter
        }

    }

    private fun validateDetails(): Boolean {

        when {
            binding.etOrderNumber.text.length != 4 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_order_number), true)
            }
            binding.etTableNumber.text.length != 3 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_table_number), true)
            }
            binding.rvOrdersList.isEmpty() -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_order), true)
            }
            else -> {
                return true
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        itemUpdateSuccess()
    }

    override fun onBackPressed() {
        binding.scannerView.visibility = View.GONE
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