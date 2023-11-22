package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Project
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProjectAdapter(private val projects: List<Project>) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val projectName: TextView = view.findViewById(R.id.project_name)
        val projectDescription: TextView = view.findViewById(R.id.project_description)
        val projectStatus: TextView = view.findViewById(R.id.project_status)
        val projectAssignedDate: TextView = view.findViewById(R.id.project_assigned_date)
        val projectCompletionDate: TextView = view.findViewById(R.id.project_completion_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lecture_viewprojectitem, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.projectName.text = project.projectName
        holder.projectDescription.text = project.npoNeedsDescription
        holder.projectStatus.text = project.projectStatus.toString()
        holder.projectAssignedDate.text = project.assignedDate?.toString() ?: "Not assigned"
        holder.projectCompletionDate.text = project.completionDate?.toString() ?: "Incomplete"
    }

    override fun getItemCount() = projects.size
}
