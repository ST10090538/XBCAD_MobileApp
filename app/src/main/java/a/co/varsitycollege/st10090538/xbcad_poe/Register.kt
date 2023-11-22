package a.co.varsitycollege.st10090538.xbcad_poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val usernameEditText = findViewById<EditText>(R.id.txtUsername)
            val emailEditText = findViewById<EditText>(R.id.txtEmail)
            val passwordEditText = findViewById<EditText>(R.id.txtPassword)
            val userTypeSelected = findViewById<Spinner>(R.id.spnRole)

            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val userType = when (userTypeSelected.selectedItem.toString()) {
                "Student" -> 1
                "Lecturer" -> 0
                "NPO" -> 2
                else -> 5
            }

            // Check if username, email, password, and user type are empty
            if (username.isEmpty()) {
                usernameEditText.error = "Username is required"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                emailEditText.error = "Email is required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Password is required"
                return@setOnClickListener
            }

            if (password.length < 8) {
                passwordEditText.error = "Password must be 8 characters or longer"
                return@setOnClickListener
            }

            // All input is valid, proceed with user registration
            val dbHelper = DBHelper().addUser(username, email, password, userType)
            dbHelper.start()
            dbHelper.join()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}