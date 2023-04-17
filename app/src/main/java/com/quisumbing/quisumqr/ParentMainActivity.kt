package com.quisumbing.quisumqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_parent_main.*
import kotlinx.android.synthetic.main.activity_postscan.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ParentMainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_main)

        parentStudentHasAttended.text = ""
        parentTextTime.text = ""
        parentTime.text = ""

        val btnparentRefresh = findViewById<Button>(R.id.parentRefreshBtn)
        btnparentRefresh.setOnClickListener {
            checkDatabase()
        }

        checkDatabase()

    }
    fun checkDatabase(){
        var parentstudentLRN = intent.getStringExtra("parentstudentLRN")

        val currentdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        database =
            FirebaseDatabase.getInstance("https://quisumqr-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Students")
        if (parentstudentLRN != null) {
            database.child(parentstudentLRN).child("Attendances").child(currentdate).get().addOnSuccessListener {
                if (it.exists()){
                    parentStudentHasAttended.text = "Your child is now in school!"
                    parentTextTime.text = "Time of attendance:"
                    parentTime.text = it.value.toString()
                } else{
                    parentStudentHasAttended.text = "Your child is not yet in school!"
                    parentTextTime.text = ""
                    parentTime.text = ""
                }}.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()

            }
        }
    }
}