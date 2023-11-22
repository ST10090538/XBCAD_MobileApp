package a.co.varsitycollege.st10090538.xbcad_poe


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity



class NpoCreateProject : AppCompatActivity() {
    private lateinit var projectTitleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var createButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.npo_create_project)

        val user = findViewById<ImageView>(R.id.createProjectUserIcon)
        user.setOnClickListener() {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }

        // Initialize UI elements
        projectTitleEditText = findViewById(R.id.txtUsernameLogin)
        descriptionEditText = findViewById(R.id.editText2)
        createButton = findViewById(R.id.button4)

        // Set click listener for the "Create" button
        createButton.setOnClickListener {
            // Retrieve input values
            val projectTitle = projectTitleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            // Get the user ID (replace with your actual logic to get the user ID)
            val userID = getUserId()

            // Save the project to the database using DBHelper
            val dbHelper = DBHelper()
            val saveProjectThread = dbHelper.createProject(projectTitle, description, userID)
            saveProjectThread.start()
            saveProjectThread.join()

            // Navigate back to the project list activity (NpoProjectsList)
            val intent = Intent(this, NpoProjectsList::class.java)
            startActivity(intent)
        }
    }

    private fun getUserId(): Int {
        val loggedInUser = GlobalData.loggedInUser
        return loggedInUser?.userID ?: -1 // Replace -1 with a default value if needed
    }

}
