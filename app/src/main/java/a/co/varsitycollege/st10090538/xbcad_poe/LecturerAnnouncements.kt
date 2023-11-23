package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Announcement
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.Locale

class LecturerAnnouncements : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: AnnouncementAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lecturer_announcments)

        val user = findViewById<ImageView>(R.id.announcementsUserIcon)
        user.setOnClickListener() {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }

        val viewGroups = findViewById<Button>(R.id.AssignGroup)
        viewGroups.setOnClickListener() {
            val intent = Intent(this, LecturerViewGroups::class.java)
            startActivity(intent)
        }

        val viewProjects = findViewById<Button>(R.id.btnViewProjects)
        viewProjects.setOnClickListener() {
            val intent = Intent(this, LecturerViewProjects::class.java)
            startActivity(intent)
        }

            findViewById<Button>(R.id.addAnnouncementButton).setOnClickListener() {
                startActivity(Intent(this, LecturerAddAnnouncement::class.java))
            }


        dbHelper = DBHelper()
        recyclerView = findViewById(R.id.announcementRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        dbHelper.getAnnouncements(object : AnnouncementsCallback {
            override fun onCallback(announcements: List<Announcement>) {
                // Initialize and set the adapter on the main thread
                runOnUiThread {
                    adapter = AnnouncementAdapter(announcements)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }
}
