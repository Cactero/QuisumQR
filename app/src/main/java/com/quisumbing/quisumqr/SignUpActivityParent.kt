package com.quisumbing.quisumqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.quisumbing.quisumqr.databinding.ActivitySignUpParentBinding
import com.quisumbing.quisumqr.databinding.ActivitySignupStudentBinding
import kotlinx.android.synthetic.main.activity_sign_up_parent.*

class SignUpActivityParent : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpParentBinding
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpParentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnsignupToLogin = findViewById<TextView>(R.id.parentBtnAlreadyRegistered)
        btnsignupToLogin.setOnClickListener({
            val signupToLogin = Intent(this, LoginActivityParent::class.java);
            startActivity((signupToLogin));
            finish();
        })


        firebaseAuth = FirebaseAuth.getInstance()

        val btnParentSignUp = findViewById<Button>(R.id.parentBtnSignUp)
        btnParentSignUp.setOnClickListener {
            val parentFirstName =
                binding.parentSignUpFirstName.getEditText()?.getText().toString()
            val parentLastName =
                binding.parentSignUpLastName.getEditText()?.getText().toString()
            val parentStudentFirstName =
                binding.parentSignUpStudentFirstName.getEditText()?.getText().toString()
            val parentStudentLastName =
                binding.parentSignUpStudentLastName.getEditText()?.getText().toString()
            val parentStudentGrade =
                binding.parentSignUpStudentGrade.getEditText()?.getText().toString()
            val parentStudentSection =
                binding.parentSignUpStudentSection.getEditText()?.getText().toString()
            val parentStudentLRN = binding.parentSignUpStudentLRN.getEditText()?.getText().toString()
            val parentEmail = binding.parentSignUpEmail.getEditText()?.getText().toString()

            if (parentFirstName.isNotEmpty() && parentLastName.isNotEmpty() && parentStudentFirstName.isNotEmpty() && parentStudentLastName.isNotEmpty()
                && parentStudentGrade.isNotEmpty() && parentStudentSection.isNotEmpty() && parentStudentLRN.isNotEmpty() && parentEmail.isNotEmpty()
            ) {
                firebaseAuth.createUserWithEmailAndPassword(parentEmail, parentStudentLRN)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            database = FirebaseDatabase.getInstance("https://quisumqr-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Parents")
                            val Parent = Parent(
                                parentFirstName,
                                parentLastName,
                                parentStudentFirstName,
                                parentStudentLastName,
                                parentStudentGrade,
                                parentStudentSection,
                                parentStudentLRN,
                                parentEmail)
                            database.child("Parent" + parentStudentLRN).child("Info").setValue(Parent).addOnSuccessListener {
                                Toast.makeText(this,"Successfully Saved", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                            }

                            val intent = Intent(this, LoginActivityParent::class.java)
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