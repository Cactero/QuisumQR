package com.quisumbing.quisumqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivityParent : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_parent)

        firebaseAuth = FirebaseAuth.getInstance()

        val btnloginToSignup = findViewById<TextView>(R.id.parentBtnNoAccount)
        btnloginToSignup.setOnClickListener({
            val loginToSignUp = Intent(this, SignUpActivityParent::class.java);
            startActivity((loginToSignUp));
            finish();
        })

        val btnparentLogin = findViewById<Button>(R.id.parentBtnLogin)
        btnparentLogin.setOnClickListener {
            val parentstudentLRN = findViewById<TextInputLayout>(R.id.parentLoginLRN).getEditText()?.getText().toString()
            val parentEmail = findViewById<TextInputLayout>(R.id.parentLoginEmail).getEditText()?.getText().toString()
            if (parentstudentLRN.isNotEmpty() && parentEmail.isNotEmpty()
            ) {
                firebaseAuth.signInWithEmailAndPassword(parentEmail,parentstudentLRN)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, ParentMainActivity::class.java)
                            intent.putExtra("parentstudentLRN", parentstudentLRN)
                            startActivity(intent)
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