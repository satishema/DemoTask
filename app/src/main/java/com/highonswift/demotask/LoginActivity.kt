package com.highonswift.demotask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var txtView: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Firebase Auth
        auth = FirebaseAuth.getInstance()

        editEmail = findViewById(R.id.editText)
        editPassword = findViewById(R.id.editPassword)
        btnLogin = findViewById(R.id.btn_login)
        btnSignUp = findViewById(R.id.btn_signup)
        txtView = findViewById(R.id.txtView)

        btnLogin.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        txtView.text = "Logged in as: ${auth.currentUser?.email}"
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, WeatherActivity::class.java)
                        startActivity(intent)
                    } else {
                        txtView.text = "Login failed"
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
        }

        btnSignUp.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        txtView.text = "Registered as: ${auth.currentUser?.email}"
                        Toast.makeText(this, "Sign Up successful!", Toast.LENGTH_SHORT).show()
                    } else {
                        txtView.text = "Sign Up failed"
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
        }
    }
}
