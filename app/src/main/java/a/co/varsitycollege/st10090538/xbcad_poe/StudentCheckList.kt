package a.co.varsitycollege.st10090538.xbcad_poe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.Date

class StudentCheckList : AppCompatActivity() {

    private lateinit var milestoneEditText: EditText
    private lateinit var addMilestoneButton: Button
    private lateinit var dbHelper: DBHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_checklist)

        val groupList = findViewById<Button>(R.id.groupListButton)
        groupList.setOnClickListener {
            val intent = Intent(this, StudentGroupList::class.java)
            startActivity(intent)
        }

        val groupChat = findViewById<Button>(R.id.btnGroupChat)
        groupChat.setOnClickListener {
            val intent = Intent(this, StudentGroupDiscussion::class.java)
            startActivity(intent)
        }

        milestoneEditText = findViewById(R.id.editTextText2)
        addMilestoneButton = findViewById(R.id.button2)

        // Initialize DBHelper
        dbHelper = DBHelper()

        // Set click listener for the "Add Milestone" button
        addMilestoneButton.setOnClickListener {
            addMilestone()
    }
}
    private fun addMilestone() {
        // Get the entered milestone text
        val milestoneText = milestoneEditText.text.toString()

        val date = Date()

        // Using dbHelper to perform database operation
        dbHelper.saveAnnouncement("username", "Milestone Title", milestoneText,java.sql.Date(date.time))

        // Clear the EditText after adding the milestone
        milestoneEditText.text.clear()
    }
}
