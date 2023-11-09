package Models

import java.sql.Date

class Group(groupID: Int, groupName: String, creationDate: Date, projectID: Int) {
    val groupID = groupID
    val groupName = groupName
    val creationDate = creationDate
    val projectID = projectID
}