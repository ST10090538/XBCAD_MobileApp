package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Group
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class GroupsAdapter(private val groups: List<Group>) : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupNameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)
        val creationDateTextView: TextView = itemView.findViewById(R.id.creationDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groups[position]

        // Display group name in bold
        val formattedGroupName = "<b>${group.groupName}</b>"
        holder.groupNameTextView.text = Html.fromHtml(formattedGroupName, Html.FROM_HTML_MODE_COMPACT)

        // Display creation date in faded text and smaller font
        val formattedCreationDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(group.creationDate)
        holder.creationDateTextView.text = "Created on: " + formattedCreationDate

        // Optionally, you can apply fading effect and adjust the font size
        holder.creationDateTextView.alpha = 0.6f
        holder.creationDateTextView.textSize = 12f
    }

    override fun getItemCount(): Int {
        return groups.size
    }
}

