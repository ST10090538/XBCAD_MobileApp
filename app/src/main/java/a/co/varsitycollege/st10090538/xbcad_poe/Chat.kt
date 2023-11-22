package a.co.varsitycollege.st10090538.xbcad_poe

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Chat : AppCompatActivity() {

    private lateinit var adapter: ChatAdapter
    private val chatList = mutableListOf<ChatItem>()
    private val dbHelper = DBHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personalchat)


        val recyclerView: RecyclerView = findViewById(R.id.my_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChatAdapter(chatList)
        recyclerView.adapter = adapter

        val messageInput: EditText = findViewById(R.id.message_input)
        val sendButton: Button = findViewById(R.id.send_button)

        sendButton.setOnClickListener {
            val messageText = messageInput.text.toString()
            if (messageText.isNotEmpty()) {
                val senderUserID = 1
                val receiverUserID = 2
                dbHelper.insertChatMessage(senderUserID, receiverUserID, messageText)
                chatList.add(
                    ChatItem(
                        dbHelper.getUsername(senderUserID),
                        messageText,
                        true
                    )
                ) // Added 'true' for isSender
                adapter.notifyDataSetChanged()
                messageInput.text.clear()
            }
        }

        val senderUserID = 1
        val receiverUserID = 2
        val studentID = intent.getIntExtra("studentID", -1)
        Log.d("ChatWithGroup", "Group ID: $studentID")
        dbHelper.getMessages(senderUserID, receiverUserID) { messages ->
            runOnUiThread {
                chatList.clear()
                for (message in messages) {
                    val username = dbHelper.getUsername(senderUserID)
                    chatList.add(ChatItem(username, message, false)) // Added 'false' for isSender
                }
                adapter.notifyDataSetChanged()
            }
        }
    }
}