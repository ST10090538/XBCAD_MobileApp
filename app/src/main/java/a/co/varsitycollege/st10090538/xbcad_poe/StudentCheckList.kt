package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Milestone
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Date

class StudentCheckList : AppCompatActivity() {

    private lateinit var milestoneEditText: EditText
    private lateinit var addMilestoneButton: Button
    private lateinit var milestoneContainer: LinearLayout
    private lateinit var dbHelper: DBHelper
    private lateinit var milestones: List<Milestone>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_checklist)


        milestones = GlobalData.milestoneList!!
        displayMilestone()

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

        dbHelper = DBHelper()

        addMilestoneButton.setOnClickListener {
            addMilestone()
        }
    }
    private fun addMilestone() {
        // Get the entered milestone text
        val milestoneText = milestoneEditText.text.toString()



        // Using dbHelper to perform database operation
        val addMileStone = dbHelper.addMilestone(milestoneText, GlobalData.project!!.projectID, GlobalData.groupID!!,  0)
        val milestone = Milestone(0,milestoneText,GlobalData.project!!.projectID,GlobalData.groupID!!,0)
        addMileStone.start()
        milestones += milestone
        // Display the added milestone dynamically
        displayMilestone()

        milestoneEditText.text.clear()
    }
    private fun displayMilestone() {

        for(milestone in milestones){
            // Create a LinearLayout to hold the milestone components (TextView and CheckBox)
            val milestoneLayout = LinearLayout(this)

            // Set layout parameters for the milestoneLayout (you can adjust the height as needed)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                250
            )
            milestoneLayout.layoutParams = layoutParams

            milestoneLayout.orientation = LinearLayout.HORIZONTAL

            // Create a CheckBox
            val checkBox = CheckBox(this)
            checkBox.isChecked = milestone.isComplete == 1
            milestoneLayout.addView(checkBox)
            checkBox.setOnCheckedChangeListener(){ _: CompoundButton, _: Boolean ->
                if(checkBox.isChecked){
                    milestone.isComplete = 1
                }
                else{
                    milestone.isComplete = 0
                }
                val helper = DBHelper().updateMilestone(milestone.desc, milestone.isComplete)
                helper.start()
            }

            // Create a TextView to display the milestone
            val milestoneTextView = TextView(this)

            // Set the text and formatting
            val formattedText = "Title: $title\n${milestone.desc}"
            milestoneTextView.text = formattedText

            // Set text color to black
            milestoneTextView.setTextColor(ContextCompat.getColor(this, R.color.black))

            // Add the TextView to the milestoneLayout
            milestoneLayout.addView(milestoneTextView)

            // Add the milestoneLayout to the milestoneContainer
            val container = findViewById<LinearLayout>(R.id.milestoneContainer)
            container.addView(milestoneLayout)

            // Log statement to check if milestone components are added
            Log.d("MilestoneAdded", "Milestone added: $formattedText")
        }
    }

}
