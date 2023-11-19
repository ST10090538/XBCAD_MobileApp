package a.co.varsitycollege.st10090538.xbcad_poe

import Models.GroupChatMessage
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class GroupChatAdapter(private val groupChatMessages: List<GroupChatMessage>) : RecyclerView.Adapter<GroupChatAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val messageText: TextView = itemView.findViewById(R.id.message_text)
        val timestamp: TextView = itemView.findViewById(R.id.timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chatwithgroup_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = groupChatMessages[position]
        holder.username.text = message.username
        holder.messageText.text = message.messageText
        holder.timestamp.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(message.timestamp)
    }

    override fun getItemCount() = groupChatMessages.size
}