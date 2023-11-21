package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Project
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.sql.SQLException
import kotlinx.coroutines.*

class NpoProjectsList : AppCompatActivity() {

    private val dbHelper = DBHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.npo_projects_list)

        // Assuming that DBHelper is properly initialized
        val linearLayout = findViewById<LinearLayout>(R.id.LinearLayoutId1)

        // Use Kotlin coroutine to fetch projects asynchronously
        CoroutineScope(Dispatchers.IO).launch {
            // Retrieve the list of projects from the database
            val getProjectsThread = dbHelper.getProjects()
            getProjectsThread.start()
            getProjectsThread.join()

            // Extract the list of projects from the thread result
            val projectsList = dbHelper.getProjectsResult

            // Update UI on the main thread
            withContext(Dispatchers.Main) {
                // Iterate through the list of projects and create TextViews dynamically
                for (project in projectsList) {
                    val projectTextView = TextView(this@NpoProjectsList)
                    projectTextView.text = project.projectName
                    projectTextView.textSize = 20f
                    projectTextView.setTextColor(ContextCompat.getColor(this@NpoProjectsList, R.color.black))

                    // Set a click listener to handle click events on the project TextView
                    projectTextView.setOnClickListener {
                        // Handle click event for the project
                        // Show the project description
                        showProjectDescription(project)
                    }

                    // Add the TextView to the LinearLayout
                    linearLayout.addView(projectTextView)
                }
            }
        }

        // Handle the "Add Project" button click
        val addProjectButton = findViewById<Button>(R.id.button2)
        addProjectButton.setOnClickListener {
            // Handle the click event, for example, navigate to the activity to add a new project
            startActivity(Intent(this, NpoCreateProject::class.java))
        }
    }

    private fun showProjectDescription(project: Project) {
        // Launch a new activity to show the project description
        val intent = Intent(this, ProjectDescription::class.java)
        intent.putExtra("PROJECT_NAME", project.projectName)
        intent.putExtra("PROJECT_DESCRIPTION", project.npoNeedsDescription)
        startActivity(intent)
    }

    fun createProject(projectName: String, needsDesc: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Retrieve the logged-in user ID from GlobalData.loggedInUser
                val userID = GlobalData.loggedInUser?.userID ?: -1

                // Call the createProject method in DBHelper
                dbHelper.createProject(projectName, needsDesc, userID)

                // Update GlobalData.projectsList with the new projects
                val getProjectsThread = dbHelper.getProjects()
                getProjectsThread.start()
                getProjectsThread.join()

                // Extract the list of projects from the thread result
                GlobalData.projectsList = dbHelper.getProjectsResult

                // Now, perform UI updates on the main thread
                withContext(Dispatchers.Main) {
                    // Optionally, you can update UI components here
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } catch (ex: SQLException) {
                ex.printStackTrace()
            }
        }
    }
}
