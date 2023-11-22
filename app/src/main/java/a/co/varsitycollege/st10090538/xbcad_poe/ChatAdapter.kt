package a.co.varsitycollege.st10090538.xbcad_poe

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val chatList: List<ChatItem>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val message: TextView = itemView.findViewById(R.id.message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentItem = chatList[position]
        holder.username.text = currentItem.username
        holder.message.text = currentItem.message

        if (currentItem.isSender) {
            holder.message.setBackgroundResource(R.drawable.sender_message_background)
            holder.message.setTextColor(Color.WHITE)
        } else {
            holder.message.setBackgroundResource(R.drawable.receiver_message_background)
            holder.message.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount() = chatList.size
}

data class ChatItem(val username: String, val message: String, val isSender: Boolean)