package Models

import java.sql.Date

class Milestone (id: Int, description: String, projectID: Int, groupID: Int, isComplete: Int) {

    val projectID = projectID
    val id = id
    val desc = description
    val groupId = groupID
    var isComplete = isComplete

}

