package a.co.varsitycollege.st10090538.xbcad_poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        withoutGroupRecyclerView = findViewById(R.id.withoutGroupRecyclerView)
        withoutGroupRecyclerView.layoutManager = LinearLayoutManager(this)
        val dbHelper = DBHelper()
        val thread = dbHelper.getGroups()
        thread.start()
        groupAdapter = GroupAdapter(GlobalData.groupList)
        recyclerView.adapter = groupAdapter
        val withoutGroupThread = dbHelper.getStudentsWithoutGroups(GlobalData.studentsWithoutGroupsList)
        withoutGroupThread.start()
        withoutGroupThread.join()
        withoutGroupAdapter = WithoutGroupAdapter(GlobalData.studentsWithoutGroupsList, dbHelper)
        withoutGroupRecyclerView.adapter = withoutGroupAdapter
    }
}
