package a.co.varsitycollege.st10090538.xbcad_poe
import Models.User
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class WithoutGroupAdapter(private val students: List<User>, private val dbHelper: DBHelper) : RecyclerView.Adapter<WithoutGroupAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.studentName)
        val messageButton: Button = itemView.findViewById(R.id.messageButton)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.withoutgroup_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.studentName.text = dbHelper.getUsername(student.userID)
        holder.messageButton.setOnClickListener {
            // Handle button click
        }
    }
    override fun getItemCount() = students.size
}