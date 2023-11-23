package a.co.varsitycollege.st10090538.xbcad_poe

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LecturerAdapter(private val lecturers: List<Lecturer>, private val onMessageButtonClick: (Lecturer) -> Unit) : RecyclerView.Adapter<LecturerAdapter.LecturerViewHolder>() {

    inner class LecturerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lecturerName: TextView = view.findViewById(R.id.lecturer_name)
        val messageButton: Button = view.findViewById(R.id.message_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LecturerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.choose_lecturer_item, parent, false)
        return LecturerViewHolder(view)
    }

    override fun onBindViewHolder(holder: LecturerViewHolder, position: Int) {
        val lecturer = lecturers[position]
        holder.lecturerName.text = lecturer.name
        holder.messageButton.setOnClickListener {
            val intent = Intent(it.context, Chat::class.java)
            val currentStudentID = it.context.getActivity()?.intent?.getIntExtra("userID", -1) ?: -1
            intent.putExtra("studentID", currentStudentID)
            intent.putExtra("lecturerID", lecturer.id)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount() = lecturers.size
}

fun Context.getActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

data class Lecturer(val id: Int, val name: String)
