package a.co.varsitycollege.st10090538.xbcad_poe


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class UserProfile : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)

        val changepassword = findViewById<Button>(R.id.changePasswordBtn)
        changepassword.setOnClickListener() {
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)

            val logout = findViewById<Button>(R.id.logoutBtn)
            logout.setOnClickListener() {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}