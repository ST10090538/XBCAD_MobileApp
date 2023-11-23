package a.co.varsitycollege.st10090538.xbcad_poe

import Tools.TLSSocketFactory
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        GlobalData.loggedInUser = null
        HttpsURLConnection.setDefaultSSLSocketFactory(TLSSocketFactory())
        findViewById<TextView>(R.id.registerText).setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            val usernameEditText = findViewById<EditText>(R.id.txtUsernameLogin)
            val passwordEditText = findViewById<EditText>(R.id.txtPasswordLogin)

            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (TextUtils.isEmpty(username)) {
                usernameEditText.error = "Username is required"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(password)) {
                passwordEditText.error = "Password is required"
                return@setOnClickListener
            }

            val dbHelper = DBHelper().loginUser(username, password)
            dbHelper.start()
            dbHelper.join()

            if (GlobalData.loggedInUser != null) {
                val user = GlobalData.loggedInUser
                when (user?.userType) {
                    0 -> {
                        val intent = Intent(this, LecturerViewGroups::class.java)
                        intent.putExtra("userID", user.userID)
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(this, StudentGroupList::class.java)
                        intent.putExtra("userID", user.userID)
                        startActivity(intent)
                    }
                    2 -> {
                        val intent = Intent(this, NpoProjectsList::class.java)
                        intent.putExtra("userID", user.userID)
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(this, "Invalid Login Credentials!", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
