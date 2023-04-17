package com.quisumbing.quisumqr

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.FirebaseDatabaseKtxRegistrar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.quisumbing.quisumqr.databinding.ActivityMainBinding
import com.quisumbing.quisumqr.databinding.ActivitySignupStudentBinding

class SignUpActivityStudent : AppCompatActivity() {

    private lateinit var binding:ActivitySignupStudentBinding
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val btnsignupToLogin = findViewById<TextView>(R.id.studentBtnAlreadyRegistered)
        btnsignupToLogin.setOnClickListener({
            val signupToLogin = Intent(this, LoginActivityStudent::class.java);
            startActivity((signupToLogin));
            finish();
        })


        firebaseAuth = FirebaseAuth.getInstance()

        val btnstudentSignUp = findViewById<Button>(R.id.studentBtnSignUp)
        btnstudentSignUp.setOnClickListener {
            val studentFirstName =
                binding.studentSignUpFirstName.getEditText()?.getText().toString()
            val studentLastName =
                binding.studentSignUpLastName.getEditText()?.getText().toString()
            val studentGrade =
                binding.studentSignUpGrade.getEditText()?.getText().toString()
            val studentSection =
                binding.studentSignUpSection.getEditText()?.getText().toString()
            val studentParentsFirstName =
                binding.studentSignUpParentsFirstName.getEditText()?.getText().toString()
            val studentParentsLastName =
                binding.studentSignUpParentsLastName.getEditText()?.getText().toString()
            //val studentPassword =
            //    binding.studentSignUpPassword.getEditText()?.getText().toString()
            //val studentConfirmPassword =
            //    binding.studentSignUpConfirmPassword.getEditText()?.getText().toString()
            val studentLRN = binding.studentSignUpLRN.getEditText()?.getText().toString()
            val studentEmail = binding.studentSignUpEmail.getEditText()?.getText().toString()



            if (studentFirstName.isNotEmpty() && studentLastName.isNotEmpty() && studentGrade.isNotEmpty() && studentSection.isNotEmpty() &&
                studentParentsFirstName.isNotEmpty() && studentParentsLastName.isNotEmpty() && studentLRN.isNotEmpty() && studentEmail.isNotEmpty()
            ) {
                //if(studentPassword == studentConfirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(studentEmail, studentLRN)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                database =
                                    FirebaseDatabase.getInstance("https://quisumqr-default-rtdb.asia-southeast1.firebasedatabase.app")
                                        .getReference("Students")
                                val Student = Student(
                                    studentFirstName,
                                    studentLastName,
                                    studentGrade,
                                    studentSection,
                                    studentLRN,
                                    studentEmail,
                                    studentParentsFirstName,
                                    studentParentsLastName,
                                )

                                database.child(studentLRN).child("Info").setValue(Student)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this,
                                            "Successfully Saved",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }.addOnFailureListener {
                                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                                }

                                Firebase.messaging.subscribeToTopic("attendance")
                                    .addOnCompleteListener { task ->
                                        var msg = "Linked"
                                        if (!task.isSuccessful) {
                                            msg = "Failed to link"
                                        }
                                        Log.d(TAG, msg)
                                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                                    }
                                val intent = Intent(this, LoginActivityStudent::class.java)
                                val extras = Bundle().apply {
                                    putString("studentGrade",studentGrade)
                                    putString("studentSection",studentSection)
                                }
                                startActivity(intent.putExtras(extras))
                            } else {
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
//                } else {
//                    Toast.makeText(this,"Password does not match!", Toast.LENGTH_SHORT).show()
//                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}