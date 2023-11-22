package a.co.varsitycollege.st10090538.xbcad_poe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LecturerViewProjects : AppCompatActivity() {

    private lateinit var projectsRecyclerView: RecyclerView
    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lecture_view_projects)

        projectsRecyclerView = findViewById(R.id.projects_recycler_view)
        projectsRecyclerView.layoutManager = LinearLayoutManager(this)

        dbHelper = DBHelper()
        val projectsThread = dbHelper.getProjects()
        projectsThread.start()

        projectsThread.join()

        val projects = dbHelper.getProjectsResult

        projectAdapter = ProjectAdapter(projects)
        projectsRecyclerView.adapter = projectAdapter
    }
}