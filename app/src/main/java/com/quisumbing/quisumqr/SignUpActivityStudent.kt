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


    //val studentFirstNameRef = database.getReference("firstname")
    //val studentLastNameRef = database.getReference("lastname")
    //val studentGradeRef = database.getReference("grade")
    //val studentSectionRef = database.getReference("section")
    //val studentEmailRef = database.getReference("email")
    //val studentLRNRef = database.getReference("lrn")
    //val studentParentsNameRef = database.getReference("parentsname")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnsignupToScanner = findViewById<Button>(R.id.studentGoToScanner)
        btnsignupToScanner.setOnClickListener({
            val signupToScanner = Intent(this, MainActivity::class.java);
            startActivity((signupToScanner));
            finish();
        })

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
            val studentParentsName =
                binding.studentSignUpParentsName.getEditText()?.getText().toString()
            //val studentPassword =
            //    binding.studentSignUpPassword.getEditText()?.getText().toString()
            //val studentConfirmPassword =
            //    binding.studentSignUpConfirmPassword.getEditText()?.getText().toString()
            val studentLRN = binding.studentSignUpLRN.getEditText()?.getText().toString()
            val studentEmail = binding.studentSignUpEmail.getEditText()?.getText().toString()



            if (studentFirstName.isNotEmpty() && studentLastName.isNotEmpty() && studentGrade.isNotEmpty() &&
                studentSection.isNotEmpty() && studentParentsName.isNotEmpty() && studentLRN.isNotEmpty() && studentEmail.isNotEmpty()
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
                                    studentParentsName
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
                                startActivity(intent)
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