package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Group
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class GroupAdapter(private val groups: List<Group>) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val groupName: TextView = view.findViewById(R.id.groupName)
        val creationDate: TextView = view.findViewById(R.id.creationDate)
        val projectName: TextView = view.findViewById(R.id.projectName)
        val studentList: LinearLayout = view.findViewById(R.id.studentList)
        val chatButton: Button = view.findViewById(R.id.chatButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groups[position]
        holder.groupName.text = group.groupName
        holder.creationDate.text = SimpleDateFormat("dd/MM/yyyy").format(group.creationDate)
        holder.projectName.text = group.projectName

        // Clear the studentList to prevent duplicate views when the ViewHolder is recycled
        holder.studentList.removeAllViews()

        // Add TextViews to the studentList for each student in the group
        for (student in group.students!!) {
            val textView = TextView(holder.itemView.context)
            textView.text = student.userName
            holder.studentList.addView(textView)
        }

        holder.chatButton.setOnClickListener {
            val intent = Intent(it.context, ChatWithGroup::class.java)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount() = groups.size
}
