package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Group
import Models.Project
import Models.User
import java.sql.Connection

object GlobalData {
    var connection: Connection? = null
    var loggedInUser: User? = null
    var groupList: List<Group> = emptyList()
    var projectsList: List<Project> = emptyList()
}