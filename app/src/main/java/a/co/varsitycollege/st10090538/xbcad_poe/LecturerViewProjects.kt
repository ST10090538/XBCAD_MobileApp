package a.co.varsitycollege.st10090538.xbcad_poe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LecturerViewProjects : AppCompatActivity() {

    private lateinit var projectsRecyclerView: RecyclerView
    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var dbHelper: DBHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lecture_view_projects)

        projectsRecyclerView = findViewById(R.id.projects_recycler_view)

        // Use GridLayoutManager to allow both horizontal and vertical scrolling
        val layoutManager = GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false)
        projectsRecyclerView.layoutManager = layoutManager

        dbHelper = DBHelper()
        val projectsThread = dbHelper.getProjects()
        projectsThread.start()

        projectsThread.join()

        val projects = dbHelper.getProjectsResult

        projectAdapter = ProjectAdapter(projects)
        projectsRecyclerView.adapter = projectAdapter

        val asign = findViewById<Button>(R.id.AssignGroup)
        asign.setOnClickListener(){
            val intent = Intent(this, LecturerViewGroups::class.java)
            startActivity(intent)
        }

        val announce = findViewById<Button>(R.id.btnAnnouncements)
        announce.setOnClickListener(){
            val intent = Intent(this, LecturerAnnouncements::class.java)
            startActivity(intent)
        }

        val user = findViewById<ImageView>(R.id.announcementsUserIcon)
        user.setOnClickListener(){
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }
    }
}