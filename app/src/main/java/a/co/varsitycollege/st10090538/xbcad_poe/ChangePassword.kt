package a.co.varsitycollege.st10090538.xbcad_poe

import Tools.Encryption
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChangePassword : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_password)

        findViewById<Button>(R.id.btnSubmit).setOnClickListener(){
            val user = GlobalData.loggedInUser!!.userID
            val newPass = Encryption.encryptData(findViewById<EditText>(R.id.txtNewPassword).text.toString())
            val confirmPass = Encryption.encryptData(findViewById<EditText>(R.id.txtConfirmPassword).text.toString())

            if(newPass == confirmPass){
                val helper = DBHelper().updatePassword(user, newPass)
                helper.start()
                helper.join()
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, UserProfile::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

    }
}