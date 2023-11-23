package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Group
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors



class StudentGroupList : AppCompatActivity(), DBHelper.OnGroupsLoadedCallback {

    private val dbHelper = DBHelper()
    private lateinit var recyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_group_list)
        recyclerView = findViewById(R.id.recyclerViewStudentGroups)
        recyclerView.layoutManager = LinearLayoutManager(this)

        refreshGroupList()
        checkJoinedGroup()


        val announcement = findViewById<Button>(R.id.announcementsBtn)
        announcement.setOnClickListener() {
            val intent = Intent(this, StudentViewAnnouncements::class.java)
            startActivity(intent)
        }

        val toUserProfile = findViewById<ImageView>(R.id.groupListUserIcon)
        toUserProfile.setOnClickListener() {
            try {
                val intent = Intent(applicationContext, UserProfile::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val groupChat = findViewById<Button>(R.id.btnGroupChat)
        groupChat.setOnClickListener {
            val intent = Intent(this, StudentGroupDiscussion::class.java)
            startActivity(intent)
        }

        val chooseLecturer = findViewById<Button>(R.id.messageLecturerBtn)
        chooseLecturer.setOnClickListener {
            val intent = Intent(this, ChooseLecturer::class.java)
            startActivity(intent)
        }

        val checkList = findViewById<Button>(R.id.checklistButton)
        checkList.setOnClickListener {
            val intent = Intent(this, StudentCheckList::class.java)
            startActivity(intent)

            val dbhelper = dbHelper.getGroups()
            GlobalData.groupList = emptyList()
            dbhelper.start()
            dbhelper.join()
            val groups = GlobalData.groupList
        }

        val groupRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewStudentGroups)
        groupRecyclerView.layoutManager = LinearLayoutManager(this)


        val createGroupButton = findViewById<Button>(R.id.createGroupButton)
        createGroupButton.setOnClickListener {
            showGroupNameInputDialog()
        }

        val joinGroupButton = findViewById<Button>(R.id.joinGroupButton)
        joinGroupButton.setOnClickListener {
            showJoinGroupInputDialog()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showGroupNameInputDialog() {
        val inputGroupName = EditText(this)

        val dialog = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setTitle("Enter Group Name")
            .setView(inputGroupName)
            .setPositiveButton("Create") { _, _ ->
                val groupName = inputGroupName.text.toString()
                val helper = DBHelper().addGroup(groupName)
                helper.start()
                helper.join()
                refreshGroupList()

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create()

        inputGroupName.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))

        inputGroupName.setTextColor(ContextCompat.getColor(this, android.R.color.black))

        dialog.show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        positiveButton.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.lightBlue
            )
        )

        negativeButton.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.lightBlue
            )
        )
    }

    private fun refreshGroupList() {
        val dbHelper = DBHelper()
        dbHelper.getGroups(object : DBHelper.OnGroupsLoadedCallback {
            override fun onGroupsLoaded(groups: List<Group>) {
                runOnUiThread {
                    val adapter = GroupsAdapter(groups)
                    recyclerView.adapter = adapter
                }
            }
        }).start()
    }

    private fun showJoinGroupInputDialog(){
        val groups = GlobalData.groupList
        val helper = DBHelper().getStudentGroups()
        helper.start()
        helper.join()
        val studentGroups = GlobalData.studentGroupList

        val groupCounts = studentGroups.groupBy { it.groupID }.mapValues { it.value.size }
        val filteredGroups = groups.filter { group -> groupCounts[group.groupID] ?: 0 <= 1 }

        val groupNames = filteredGroups.map { it.groupName }
        var Gid: Int? = null

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groupNames)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val joinGroup = Spinner(this)
        joinGroup.adapter = adapter

        joinGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGroupId = filteredGroups[position].groupID
                Gid = selectedGroupId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        val dialog = AlertDialog.Builder(this)
            .setTitle("Join Group")
            .setView(joinGroup)
            .setPositiveButton("Join") { _, _ ->
                addStudentToGroupInDatabase(Gid!!)
                checkJoinedGroup()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create()

        dialog.show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        positiveButton.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.lightBlue
            )
        )


        negativeButton.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.lightBlue
            )
        )
    }

    private fun addStudentToGroupInDatabase(groupID: Int){
        val helper = DBHelper().enrollStudentInGroup(groupID, GlobalData.loggedInUser!!.userID)
        helper.start()
        helper.join()

        var groupName: String? = null

        for (group in GlobalData.groupList) {
            if (group.groupID == groupID) {
                groupName = group.groupName
                break
            }
        }
        Toast.makeText(this, "Joined ${groupName!!}", Toast.LENGTH_SHORT).show()
    }

    private fun checkJoinedGroup(){
        var joinButton = findViewById<Button>(R.id.joinGroupButton)
        val helper = DBHelper().getStudentGroups()
        GlobalData.studentGroupList = emptyList()
        helper.start()
        helper.join()

        val studentIdToCheck = GlobalData.loggedInUser!!.userID

        val isStudentIdFound = GlobalData.studentGroupList?.any { it.studentID == studentIdToCheck } ?: false

        if (isStudentIdFound) {
            joinButton.visibility = View.INVISIBLE
        } else {
            joinButton.visibility = View.VISIBLE
        }


    }

    override fun onGroupsLoaded(groups: List<Group>) {
        TODO("Not yet implemented")
    }

}



