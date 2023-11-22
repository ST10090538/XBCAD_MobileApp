package a.co.varsitycollege.st10090538.xbcad_poe

import Models.User

interface StudentsCallback {
    fun onCallback(studentsWithoutGroups: List<User>)
}
