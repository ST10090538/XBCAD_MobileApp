package a.co.varsitycollege.st10090538.xbcad_poe

import Models.GroupChatMessage

interface GroupChatCallback {
    fun onCallback(groupChatMessages: List<GroupChatMessage>)
}
