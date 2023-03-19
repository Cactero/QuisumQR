package com.quisumbing.quisumqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class UserPrompt : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_prompt)

        val btnUserPromptStudent = findViewById<Button>(R.id.btnuserPromptStudent)
        btnUserPromptStudent.setOnClickListener({
            val userPromptStudent = Intent(this, SignUpActivityStudent::class.java);
            startActivity((userPromptStudent));
            finish();
        })

        val btnUserPromptParent = findViewById<Button>(R.id.btnuserPromptParent)
        btnUserPromptParent.setOnClickListener({
            val userPromptParent = Intent(this, SignUpActivityParent::class.java);
            startActivity((userPromptParent));
            finish();
        })
    }
}

