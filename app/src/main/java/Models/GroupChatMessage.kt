package Models

import java.time.LocalDateTime

data class GroupChatMessage(
    var id: Int,
    var groupID: Int,
    var userID: Int,
    var username: String,
    var messageText: String,
    var timestamp: java.util.Date
)