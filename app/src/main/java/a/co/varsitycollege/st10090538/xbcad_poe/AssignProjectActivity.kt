package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Group
import Models.Project
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AssignProjectActivity : AppCompatActivity() {

    private lateinit var groupSpinner: Spinner
    private lateinit var projectSpinner: Spinner
    private lateinit var assignButton: Button
    private lateinit var dbHelper: DBHelper

    // Maps to store the IDs associated with each name
    private val groupNameToIdMap = mutableMapOf<String, Int>()
    private val projectNameToIdMap = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.assignproject)

        groupSpinner = findViewById(R.id.groupID_spinner)
        projectSpinner = findViewById(R.id.projectID_spinner)
        assignButton = findViewById(R.id.assign_button)
        dbHelper = DBHelper()

        populateSpinners()

        assignButton.setOnClickListener {
            val selectedGroupName = groupSpinner.selectedItem as String
            val selectedProjectName = projectSpinner.selectedItem as String


            val selectedGroupId = groupNameToIdMap[selectedGroupName]
            val selectedProjectId = projectNameToIdMap[selectedProjectName]

            if (selectedGroupId != null && selectedProjectId != null) {
                dbHelper.assignProject(selectedGroupId, selectedProjectId).start()
                val intent = Intent(this, LecturerViewGroups::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please Select a Group and a Project", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun populateSpinners() {
        val projectsThread = dbHelper.getProjects()
        projectsThread.start()
        projectsThread.join()

        dbHelper.getProjectsResult.forEach {
            projectNameToIdMap[it.projectName] = it.projectID
        }
        val projectNames = projectNameToIdMap.keys.toList()

        val projectAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, projectNames)
        projectSpinner.adapter = projectAdapter

        dbHelper.getGroups(object : GroupsCallback {
            override fun onCallback(groups: List<Group>) {
                groups.forEach {
                    groupNameToIdMap[it.groupName] = it.groupID
                }
                val groupNames = groupNameToIdMap.keys.toList()

                runOnUiThread {
                    val groupAdapter = ArrayAdapter(
                        this@AssignProjectActivity,
                        android.R.layout.simple_spinner_item,
                        groupNames
                    )
                    groupSpinner.adapter = groupAdapter
                }
            }
        })
    }
}

