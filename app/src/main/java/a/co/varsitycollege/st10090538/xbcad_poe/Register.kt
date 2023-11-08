package a.co.varsitycollege.st10090538.xbcad_poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity


class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        findViewById<Button>(R.id.btnRegister).setOnClickListener() {
            val username = findViewById<EditText>(R.id.txtUsername).text.toString()
            val email = findViewById<EditText>(R.id.txtEmail).text.toString()
            val password = findViewById<EditText>(R.id.txtPassword).text.toString()
            val userTypeSelected = findViewById<Spinner>(R.id.spnRole)
            var userType = 5;

            if (userTypeSelected.selectedItem.toString() == "Student") {
                userType = 1;
            }
            if (userTypeSelected.selectedItem.toString() == "Lecturer") {
                userType = 0;
            }
            if (userTypeSelected.selectedItem.toString() == "NPO") {
                userType = 2;
            }

            val dbHelper = DBHelper().addUser(username,email,password, userType)
            dbHelper.start()
            dbHelper.join()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
