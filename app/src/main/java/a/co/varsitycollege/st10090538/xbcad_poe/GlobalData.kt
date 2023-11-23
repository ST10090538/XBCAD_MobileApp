package a.co.varsitycollege.st10090538.xbcad_poe

import Models.*
import java.sql.Connection

object GlobalData {
    var connection: Connection? = null
    var loggedInUser: User? = null
    var groupList: List<Group> = emptyList()
    var projectsList: List<Project> = emptyList()
    var announcementList: List<Announcement> = emptyList()
    var groupMessageList: List<GroupChatMessage> = emptyList()
    var studentGroupList: List<StudentGroup> = emptyList()
    var studentsWithoutGroupsList: MutableList<User> = mutableListOf()
    var studentsList: MutableList<User> = mutableListOf()
    var groupID: Int? = null
}
