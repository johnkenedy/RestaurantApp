package com.example.restaurantapp.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var codeScanner: CodeScanner

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
            validateDetails()
        }

    }

    private fun validateDetails(): Boolean {

        if (binding.etOrderNumber.text.length != 4) {
            showErrorSnackBar(resources.getString(R.string.err_msg_enter_order_number), true)
        } else if (binding.etTableNumber.text.length != 3) {
            showErrorSnackBar(resources.getString(R.string.err_msg_enter_table_number), true)
        }
            return true
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