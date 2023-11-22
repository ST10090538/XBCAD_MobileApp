package a.co.varsitycollege.st10090538.xbcad_poe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Date

class LecturerAddAnnouncement : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lecturer_add_announcment)

        val dbHelper = DBHelper()
        val submitButton: Button = findViewById(R.id.button4)
        val titleInput: EditText = findViewById(R.id.txtUsernameLogin)
        val contentInput: EditText = findViewById(R.id.editText2)
        val user: ImageView = findViewById(R.id.addAnnouncmentUserIcon)

        user.setOnClickListener() {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }

        submitButton.setOnClickListener {
            val title = titleInput.text.toString()
            val content = contentInput.text.toString()
            val date = Date()

            if (title.isNotBlank() && content.isNotBlank()) {
                dbHelper.saveAnnouncement("username", title, content, java.sql.Date(date.time))
                findViewById<Button>(R.id.btnAnnouncements).setOnClickListener(){
                    startActivity(Intent(this, LecturerAnnouncements::class.java))
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

    }
}