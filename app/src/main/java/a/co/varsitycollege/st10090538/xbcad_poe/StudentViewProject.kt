package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Project
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentViewProject: AppCompatActivity() {

    private val dbHelper = DBHelper()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_view_project)

        val user = findViewById<ImageView>(R.id.userIcon)
        user.setOnClickListener() {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }

        val linearLayout = findViewById<LinearLayout>(R.id.LinearLayoutId2)


        // Use Kotlin coroutine to fetch projects asynchronously
        CoroutineScope(Dispatchers.IO).launch {
            // Retrieve the list of projects from the database
            val getProjectsThread = dbHelper.getProjects()
            getProjectsThread.start()
            getProjectsThread.join()

            val groupID = GlobalData.groupID
            val projectID = GlobalData.groupList?.first{it.groupID == groupID}?.projectID

            // Extract the list of projects from the thread result
            val projectsList = dbHelper.getProjectsResult

            val project: Project? = projectsList?.first{it.projectID == projectID}?:null


            // Update UI on the main thread
            withContext(Dispatchers.Main) {
                if (project != null) {
                    showProjectDescription(project)
                }
            }


        }
        }


    private fun showProjectDescription(project: Project) {
        val intent = Intent(this, ProjectDescription::class.java)
        intent.putExtra("PROJECT_NAME", project.projectName)
        intent.putExtra("PROJECT_DESCRIPTION", project.npoNeedsDescription)
        intent.putExtra("PROJECT_STATUS", project.projectStatus)
        intent.putExtra("PROJECT_ASSIGNED_DATE", project.assignedDate?.time ?: 0) // Assuming assignedDate is a Date object
        intent.putExtra("PROJECT_COMPLETION_DATE", project.completionDate?.time ?: 0) // Assuming completionDate is a Date object
        intent.putExtra("PROJECT_USER_ID", project.userID)
        startActivity(intent)
    }

}