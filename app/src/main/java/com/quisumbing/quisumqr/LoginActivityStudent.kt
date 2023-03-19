package com.quisumbing.quisumqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivityStudent : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var loggedinStudent : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_student)

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
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent.putExtra("loggedinStudent",loggedinStudent))
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