package com.quisumbing.quisumqr

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.quisumbing.quisumqr.MainActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LoginActivityStudent : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var loggedinStudent : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_student)

        val mainActivity = MainActivity()
        firebaseAuth = FirebaseAuth.getInstance()

        val btnloginToSignup = findViewById<TextView>(R.id.studentBtnNoAccount)
        btnloginToSignup.setOnClickListener({
            val loginToSignUp = Intent(this, SignUpActivityStudent::class.java);
            startActivity((loginToSignUp));
            finish();
        })

        val btnstudentLogin = findViewById<Button>(R.id.studentBtnLogin)
        btnstudentLogin.setOnClickListener {
            val studentLRN = findViewById<TextInputLayout>(R.id.studentLoginLRN).getEditText()?.getText().toString()
            val studentEmail = findViewById<TextInputLayout>(R.id.studentLoginEmail).getEditText()?.getText().toString()
            if (studentLRN.isNotEmpty() && studentEmail.isNotEmpty()
            ) {
                firebaseAuth.signInWithEmailAndPassword(studentEmail,studentLRN)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            loggedinStudent = studentLRN
                            val currentdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                            database =
                                FirebaseDatabase.getInstance("https://quisumqr-default-rtdb.asia-southeast1.firebasedatabase.app")
                                    .getReference("Students")
                            database.child(loggedinStudent).child("Attendances").child(currentdate).get().addOnSuccessListener {
                                if (it.exists()){
                                    val scanTime = it.child(currentdate).value.toString()
                                    val nextActivity = Intent(this, PostScan::class.java)
                                    nextActivity.putExtra("scanTime",scanTime)
                                    nextActivity.putExtra("loggedinStudent",loggedinStudent)
                                    startActivity(nextActivity)
                                } else{
                                    val nextActivity = Intent(this, MainActivity::class.java)
                                    startActivity(nextActivity.putExtra("loggedinStudent",loggedinStudent))
                                }}.addOnFailureListener {
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }

                    }
            } else {
                Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}