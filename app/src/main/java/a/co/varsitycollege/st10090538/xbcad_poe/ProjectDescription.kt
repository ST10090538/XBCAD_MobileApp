package a.co.varsitycollege.st10090538.xbcad_poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.sql.Date
import java.text.SimpleDateFormat

class ProjectDescription : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.project_description)

        // Retrieve project details from intent
        val projectName = intent.getStringExtra("PROJECT_NAME")
        val projectDescription = intent.getStringExtra("PROJECT_DESCRIPTION")
        val projectStatus = intent.getIntExtra("PROJECT_STATUS", 0)
        val assignedDateMillis = intent.getLongExtra("PROJECT_ASSIGNED_DATE", 0)
        val completionDateMillis = intent.getLongExtra("PROJECT_COMPLETION_DATE", 0)
        val userID = intent.getIntExtra("PROJECT_USER_ID", -1)

        // Format date strings
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val assignedDateString = if (assignedDateMillis != 0L) {
            sdf.format(Date(assignedDateMillis))
        } else {
            "Incomplete"
        }

        val completionDateString = if (completionDateMillis != 0L) {
            sdf.format(Date(completionDateMillis))
        } else {
            "" // Show as blank when completion date is null
        }

        val statusText = if (projectStatus == 0) {
            "Project Status: Incomplete"
        } else {
            "Project Status: $projectStatus"
        }


        // Display project details
        val projectNameTextView = findViewById<TextView>(R.id.textViewProjectName)
        val projectDescriptionTextView = findViewById<TextView>(R.id.textViewProjectDescription)
        val projectStatusTextView = findViewById<TextView>(R.id.textViewProjectStatus)
        val assignedDateTextView = findViewById<TextView>(R.id.textViewProjectAssignedDate)
        val completionDateTextView = findViewById<TextView>(R.id.textViewProjectCompletionDate)

        projectNameTextView.text = "Project Name: $projectName"
        projectDescriptionTextView.text = projectDescription
        projectStatusTextView.text = statusText
        assignedDateTextView.text = "Assigned Date: $assignedDateString"
        completionDateTextView.text = "Completion Date: $completionDateString"

        // Add a button to go back to the NpoProjectsList activity
        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            finish() // Close the current activity and go back to the previous one (NpoProjectsList)
        }

        val user = findViewById<ImageView>(R.id.userIcon)
        user.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }
    }
}
