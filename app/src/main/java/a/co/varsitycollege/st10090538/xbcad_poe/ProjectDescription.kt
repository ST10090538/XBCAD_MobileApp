package a.co.varsitycollege.st10090538.xbcad_poe

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProjectDescription : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.project_description)

        // Retrieve project details from intent
        val projectName = intent.getStringExtra("PROJECT_NAME")
        val projectDescription = intent.getStringExtra("PROJECT_DESCRIPTION")

        // Display project details
        val projectNameTextView = findViewById<TextView>(R.id.textViewProjectName)
        val projectDescriptionTextView = findViewById<TextView>(R.id.textViewProjectDescription)

        projectNameTextView.text = projectName
        projectDescriptionTextView.text = projectDescription

        // Add a button to go back to the NpoProjectsList activity
        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            finish() // Close the current activity and go back to the previous one (NpoProjectsList)
        }
    }
}
