package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Group
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class GroupAdapter(private val groupList: List<Group>) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupName: TextView = itemView.findViewById(R.id.groupName)
        val creationDate: TextView = itemView.findViewById(R.id.creationDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groupList[position]
        holder.groupName.text = group.groupName
        holder.creationDate.text = SimpleDateFormat("dd/MM/yyyy").format(group.creationDate)
    }

    override fun getItemCount() = groupList.size
}
