package Models

import java.sql.Date

class Project(projectID: Int, projectName: String, npoNeedsDescription: String, projectStatus: Int, assignedDate: Date?,
    completionDate: Date?, userID: Int) {
    val projectID = projectID
    val projectName = projectName
    val npoNeedsDescription = npoNeedsDescription
    val projectStatus = projectStatus
    val assignedDate = assignedDate
    val completionDate = completionDate
    val userID = userID
}