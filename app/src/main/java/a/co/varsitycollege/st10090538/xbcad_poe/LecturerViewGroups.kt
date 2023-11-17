package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Group
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.sql.Date

class LecturerViewGroups : AppCompatActivity() {

    private lateinit var groupAdapter: GroupAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lecturer_view_groups)

        findViewById<Button>(R.id.btnAnnouncements).setOnClickListener(){
            startActivity(Intent(this, LecturerAnnouncements::class.java))
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DBHelper()
        val thread = dbHelper.getGroups()
        thread.start()
        thread.join()

        groupAdapter = GroupAdapter(GlobalData.groupList)
        recyclerView.adapter = groupAdapter
    }
}
