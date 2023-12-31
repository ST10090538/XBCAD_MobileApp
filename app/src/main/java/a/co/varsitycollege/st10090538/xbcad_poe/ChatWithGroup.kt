package a.co.varsitycollege.st10090538.xbcad_poe

import Models.GroupChatMessage
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatWithGroup : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private var groupChatMessages: MutableList<GroupChatMessage> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatwithgroup)

        dbHelper = DBHelper()
        recyclerView = findViewById(R.id.my_recycler_view)
        messageInput = findViewById(R.id.message_input)
        sendButton = findViewById(R.id.send_button)

        val groupId = intent.getIntExtra("groupId", 0)

        Log.d("ChatWithGroup", "Group ID: $groupId")
        dbHelper.getGroupMessages(groupId, object : GroupChatCallback {
            override fun onCallback(messages: List<GroupChatMessage>) {
                groupChatMessages.clear()
                groupChatMessages.addAll(messages)
                runOnUiThread {
                    (recyclerView.adapter as GroupChatAdapter).notifyDataSetChanged()
                }
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = GroupChatAdapter(groupChatMessages)

        val sharedPref = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val senderUserID = sharedPref.getInt("UserID", 0)

        sendButton.setOnClickListener {
            val messageText = messageInput.text.toString()
            if (messageText.isNotEmpty()) {
                dbHelper.saveGroupMessage(groupId, senderUserID, messageText, java.util.Date())
                messageInput.text.clear()
                dbHelper.getGroupMessages(groupId, object : GroupChatCallback {
                    override fun onCallback(messages: List<GroupChatMessage>) {
                        groupChatMessages.clear()
                        groupChatMessages.addAll(messages)
                        runOnUiThread {
                            (recyclerView.adapter as GroupChatAdapter).notifyDataSetChanged()
                            recyclerView.scrollToPosition(groupChatMessages.size - 1) // Scroll to the bottom to show the latest message
                        }
                    }
                })
            }
        }
    }
}
