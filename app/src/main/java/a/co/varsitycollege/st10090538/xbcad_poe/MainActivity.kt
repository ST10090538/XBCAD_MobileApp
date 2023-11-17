package a.co.varsitycollege.st10090538.xbcad_poe

import Tools.TLSSocketFactory
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        findViewById<TextView>(R.id.registerText).setOnClickListener(){
            startActivity(Intent(this, Register::class.java))
        }

        findViewById<Button>(R.id.loginButton).setOnClickListener(){
            val username = findViewById<EditText>(R.id.txtUsernameLogin).text
            val password = findViewById<EditText>(R.id.txtPasswordLogin).text

            val dbHelper = DBHelper().loginUser(username.toString(),password.toString())
            dbHelper.start()
            dbHelper.join()

            if(GlobalData.loggedInUser != null){
                val user = GlobalData.loggedInUser
                if(user?.userType == 0){
                    startActivity(Intent(this, LecturerViewGroups::class.java))
                }
                if(user?.userType == 1){
                    startActivity(Intent(this, StudentGroupList::class.java))
                }
                if(user?.userType == 2){
                    startActivity(Intent(this, NpoProjectsList::class.java))
                }
            }
            else{
                Toast.makeText(this, "Invalid Login Credentials!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
