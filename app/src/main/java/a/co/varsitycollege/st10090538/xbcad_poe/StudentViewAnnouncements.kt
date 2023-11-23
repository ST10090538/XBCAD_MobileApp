package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Announcement
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch





class StudentViewAnnouncements: AppCompatActivity() {


    private lateinit var announcementAdapter: AnnouncementAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAnnouncements: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_view_announcements)


        // Reference views
        recyclerView = findViewById<RecyclerView>(R.id.announcementRecyclerView)
        //findViewById<Button>(R.id.viewAnnouncementButton).setOnClickListener() {
        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)


        viewAnnouncements = findViewById(R.id.viewAnnouncementButton)



        viewAnnouncements.setOnClickListener {
            Log.d("StudentViewAnnouncements", "Button clicked!")
            retrieveAnnouncements()

        }
        // Retrieve announcements from the database

    }



    private fun retrieveAnnouncements() {
        Log.d("StudentViewAnnouncements", "retrieveAnnouncements function called")
        try {
            // Use DBHelper to get announcements from the database
            val dbHelper = DBHelper()

            dbHelper.getAnnouncements(object : AnnouncementsCallback {
                override fun onCallback(announcements: List<Announcement>) {
                    runOnUiThread {
                        Log.d(
                            "StudentViewAnnouncements",
                            "Received ${announcements.size} announcements"
                        )
                        // Create and set the adapter
                        announcementAdapter = AnnouncementAdapter(announcements)
                        recyclerView.adapter = announcementAdapter


                    }
                }
            })
        } catch (e: Exception) {
            Log.e("StudentViewAnnouncements", "Error retrieving announcements: ${e.message}")
            e.printStackTrace()
        }
    }



}