package com.quisumbing.quisumqr

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.type.DateTime
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val CAMERA_REQUEST_CODE = 101

class MainActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //Signup Button

        //Device ID

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            val token = task.result
            System.out.println("TOKEN: " + token)

        })

        val btnscannerGoToSignup = findViewById<Button>(R.id.scannerGoToSignUp)
        btnscannerGoToSignup.setOnClickListener {
            val scannerGoToSignup = Intent(this, SignUpActivityStudent::class.java);
            startActivity((scannerGoToSignup));
            finish();
        }

        setupPermissions()
        codeScanner()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, scanner_view)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.TWO_DIMENSIONAL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    tv_textView.text = it.text
                    isValid()
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Camera initialization error: ${it.message}")
                }
            }
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun isValid() {
        if (tv_textView.text.startsWith("MSHS"))
        {
            var loggedinStudent = intent.getStringExtra("loggedinStudent")
            val currentdate = LocalDateTime.now()
            val dayformatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            val timeformatter = DateTimeFormatter.ofPattern("HHmmss")
            val formattedDay = currentdate.format(dayformatter)
            val formattedTime = currentdate.format(timeformatter)
            if (loggedinStudent != null) {
                database = FirebaseDatabase.getInstance("https://quisumqr-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students")
                database.child(loggedinStudent).child("Attendances").child(formattedDay).setValue(formattedTime.toString()).addOnSuccessListener {
                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show() }
            }
            tv_confirmation.text = "Attendance logged!"
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
        android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You need the camera permission to be able to use this app!", Toast.LENGTH_SHORT).show()
                } else {
                    //successful
                }
            }
        }
    }
}