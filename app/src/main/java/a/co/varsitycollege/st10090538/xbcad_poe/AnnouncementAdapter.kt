package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Announcement
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnnouncementAdapter(private val announcementList: List<Announcement>) : RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {

    class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val title: TextView = itemView.findViewById(R.id.title)
        val content: TextView = itemView.findViewById(R.id.content)
        val date: TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.announcement_item, parent, false)
        return AnnouncementViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        Log.d("AnnouncementAdapter", "Binding view holder for position $position")
        val currentAnnouncement = announcementList[position]
        holder.username.text = currentAnnouncement.username
        holder.title.text = currentAnnouncement.title
        holder.content.text = currentAnnouncement.content
        holder.date.text = currentAnnouncement.date.toString()
    }

    override fun getItemCount(): Int {
        val count = announcementList.size
        Log.d("AnnouncementAdapter", "Item count: $count")
        return count
    }
}
