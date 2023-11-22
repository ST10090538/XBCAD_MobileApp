package Models

import java.sql.Date

class Group(
    val groupID: Int,
    val groupName: String,
    val creationDate: Date,
    val projectID: Int,
    val projectName: String?,
    val students: List<User>?
)
