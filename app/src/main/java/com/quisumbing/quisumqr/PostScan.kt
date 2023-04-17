package com.quisumbing.quisumqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_postscan.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PostScan : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postscan)

        var loggedinStudent = intent.getStringExtra("loggedinStudent")
        var scanTime = intent.getStringExtra("scanTime")

        postscanTime.text = scanTime

        val currentdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        database =
            FirebaseDatabase.getInstance("https://quisumqr-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Students")
        if (loggedinStudent != null) {
            database.child(loggedinStudent).child("Attendances").child(currentdate).get().addOnSuccessListener {
                if (it.exists()){
                    postscanTime.text = it.value.toString()
                } else{
                }}.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }}