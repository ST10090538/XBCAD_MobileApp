package a.co.varsitycollege.st10090538.xbcad_poe

import Models.GroupChatMessage
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class StudentGroupDiscussion : AppCompatActivity() {

    private lateinit var recyclerViewGroupChat: RecyclerView
    private lateinit var editTextGroupChat: EditText
    private lateinit var buttonSend: Button
    private lateinit var dbHelper: DBHelper
    private lateinit var groupAdapter: GroupAdapter
    private val messages: MutableList<String> = mutableListOf()

     @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_group_discussion)

         recyclerViewGroupChat = findViewById(R.id.recyclerViewGroupChat)
        editTextGroupChat = findViewById(R.id.editTextText2)
        buttonSend = findViewById(R.id.button2)

        // Initialize DBHelper
        dbHelper = DBHelper()

        // Initialize RecyclerView and its adapter

        groupAdapter = GroupAdapter(messages)
        recyclerViewGroupChat.layoutManager = LinearLayoutManager(this)
        recyclerViewGroupChat.adapter = groupAdapter

        // Set click listener for the "Send" button
        buttonSend.setOnClickListener {
            val messageText = editTextGroupChat.text.toString()
            if (messageText.isNotEmpty()) {
                // Save the message to the database
                dbHelper.saveGroupChatMessage("username", "Group Name", messageText)
                // Clear the EditText after sending the message
                editTextGroupChat.text.clear()
                // Update the RecyclerView with the new message
                messages.add("username: $messageText")
                groupAdapter.notifyDataSetChanged()
    }
}
         val groupID = intent.getIntExtra("groupID", -1)
        dbHelper.getGroupMessages(groupID)
  }
         @SuppressLint("NotifyDataSetChanged")
    fun onSendButtonClick(view: View) {
        val messageText = editTextGroupChat.text.toString()
        if (messageText.isNotEmpty()) {
            val username = "username" //Replace the user chosen username
            val groupName = "Group Name" // Replace with students group  name
            // Save the message to the database
            dbHelper.saveGroupChatMessage(username, groupName, messageText)
            // Clear the EditText after sending the message
            editTextGroupChat.text.clear()
            // Update the RecyclerView with the new message
            messages.add("$username: $messageText")
            groupAdapter.notifyDataSetChanged()
        }
    }
}
