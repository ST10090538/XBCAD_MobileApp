package a.co.varsitycollege.st10090538.xbcad_poe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors



class StudentGroupList : AppCompatActivity() {

    private val dbHelper = DBHelper()
    
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_group_list)

        val groupChat = findViewById<Button>(R.id.btnGroupChat)
        groupChat.setOnClickListener {
            val intent = Intent(this, StudentGroupDiscussion::class.java)
            startActivity(intent)
        }

        val checkList = findViewById<Button>(R.id.checklistButton)
        checkList.setOnClickListener {
            val intent = Intent(this, StudentCheckList::class.java)
            startActivity(intent)
        }

        val groupRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewStudentGroups)
        groupRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch student groups from the database
        Executors.newSingleThreadExecutor().execute {
            dbHelper.getStudentGroups().start()

               val groupList = GlobalData.groupList

            runOnUiThread {
                // Create and set the adapter
                val groupAdapter = GroupAdapter(groupList)
                groupRecyclerView.adapter = groupAdapter
            }
        }
        val createGroupButton = findViewById<Button>(R.id.createGroupButton)
        createGroupButton.setOnClickListener {
            // Show a dialog to get the group name
            showGroupNameInputDialog()
        }

    }

@RequiresApi(Build.VERSION_CODES.O)
    private fun showGroupNameInputDialog() {

        val inputGroupName = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Enter Group Name")
            .setView(inputGroupName)
            .setPositiveButton("Create") { _, _ ->
                val groupName = inputGroupName.text.toString()

                // Call the method to add the new group to the database
                addNewGroupToDatabase(groupName)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create()

        dialog.show()
    }

     @RequiresApi(Build.VERSION_CODES.O)
    private fun addNewGroupToDatabase(groupName: String) {
        Executors.newSingleThreadExecutor().execute {
            dbHelper.addGroup(groupName) { success ->

                runOnUiThread {
                    if (success) {
                        // Refresh the group list after adding the new group
                        refreshGroupList()
                    } else {
                        Toast.makeText(this, "Failed to create a new group", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

            }
        }
    }

    private fun refreshGroupList() {
        // Fetch student groups from the database
        Executors.newSingleThreadExecutor().execute {
            dbHelper.getStudentGroups().start()


            val groupList = GlobalData.groupList

            runOnUiThread {
                // Update the existing adapter with the new group list
                val groupAdapter = GroupAdapter(groupList)

                val groupRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewStudentGroups)
                groupRecyclerView.adapter = groupAdapter
            }
        }

    }

}



