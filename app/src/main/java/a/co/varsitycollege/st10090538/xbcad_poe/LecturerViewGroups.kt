package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Group
import Models.User
import a.co.varsitycollege.st10090538.xbcad_poe.GlobalData.studentsWithoutGroupsList
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LecturerViewGroups : AppCompatActivity() {

    private lateinit var groupAdapter: GroupAdapter
    private lateinit var withoutGroupAdapter: WithoutGroupAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var withoutGroupRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lecturer_view_groups)

        val user = findViewById<ImageView>(R.id.viewGroupsUserIcon)
        user.setOnClickListener() {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnAnnouncements).setOnClickListener(){
            startActivity(Intent(this, LecturerAnnouncements::class.java))
        }

        findViewById<Button>(R.id.AssignGroup).setOnClickListener {
            startActivity(Intent(this, AssignProjectActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewProjects).setOnClickListener {
            startActivity(Intent(this, LecturerViewProjects::class.java))
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        withoutGroupRecyclerView = findViewById(R.id.withoutGroupRecyclerView) // Assuming you have another RecyclerView for students without groups
        withoutGroupRecyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DBHelper()

        dbHelper.getGroups(object : GroupsCallback {
            override fun onCallback(groups: List<Group>) {
                runOnUiThread {
                    groupAdapter = GroupAdapter(groups)
                    recyclerView.adapter = groupAdapter
                }
            }
        })

        dbHelper.getStudentsWithoutGroups(object : StudentsCallback {
            override fun onCallback(studentsWithoutGroups: List<User>) {
                runOnUiThread {
                    withoutGroupAdapter = WithoutGroupAdapter(studentsWithoutGroups)
                    withoutGroupRecyclerView.adapter = withoutGroupAdapter
                }
            }
        })

    }
}
