package a.co.varsitycollege.st10090538.xbcad_poe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Date

class StudentCheckList : AppCompatActivity() {

    private lateinit var milestoneEditText: EditText
    private lateinit var addMilestoneButton: Button
    private lateinit var milestoneContainer: LinearLayout
    private lateinit var dbHelper: DBHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_checklist)

        val announcement = findViewById<Button>(R.id.announcementsBtn)
        announcement.setOnClickListener() {
            val intent = Intent(this, StudentViewAnnouncements::class.java)
            startActivity(intent)
        }

        val chooseLecturer = findViewById<Button>(R.id.messageLecturerBtn)
        chooseLecturer.setOnClickListener {
            val intent = Intent(this, ChooseLecturer ::class.java)
            startActivity(intent)
        }

        val user = findViewById<ImageView>(R.id.checklistUserIcon)
        user.setOnClickListener() {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }

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
        milestoneContainer = findViewById(R.id.milestoneContainer)

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

        // Get the current date
        val date = Date()

        // Using dbHelper to perform database operation
        dbHelper.saveAnnouncement("username", "Milestone Title", milestoneText,  java.sql.Date(date.time))

        // Display the added milestone dynamically
        displayMilestone("Milestone Title", milestoneText, date)

        // Clear the EditText after adding the milestone
        milestoneEditText.text.clear()
        // Get the entered milestone text
        /* val milestoneText = milestoneEditText.text.toString()

         // Get the current date
         val date = Date()

         // Using dbHelper to perform database operation
         dbHelper.saveAnnouncement("username", "Milestone Title", milestoneText,  java.sql.Date(date.time))

         // Display the added milestone dynamically
         displayMilestone("Milestone Title", milestoneText, date)

         // Clear the EditText after adding the milestone
         milestoneEditText.text.clear()*/
    }
    private fun displayMilestone(title: String, text: String, date: Date) {

        // Create a TextView to display the milestone
        val milestoneTextView = TextView(this)

        // Set the text and formatting
        val formattedText = "Title: $title\n$text\nDate: $date"
        milestoneTextView.text = formattedText

        // Add the TextView to the milestoneContainer
        //findViewById<LinearLayout>(R.id.milestoneContainer).addView(milestoneTextView)
        val container = findViewById<LinearLayout>(R.id.milestoneContainer)
        container.addView(milestoneTextView)

        // Log statement to check if TextView is added
        Log.d("MilestoneAdded", "Milestone added: $formattedText")
    }
    /*
    // Create a TextView to display the milestone
    val milestoneTextView = TextView(this)

    // Set the text and formatting
    val formattedText = "Title: $title\n$text\nDate: $date"
    milestoneTextView.text = formattedText

    // Set gravity to center the text both horizontally and vertically
    milestoneTextView.gravity = Gravity.CENTER

    // Add the TextView to the container
    milestoneContainer.addView(milestoneTextView)
    findViewById<LinearLayout>(R.id.milestoneContainer).addView(milestoneTextView)*/
}
