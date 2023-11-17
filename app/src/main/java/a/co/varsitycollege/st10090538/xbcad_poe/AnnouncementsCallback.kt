package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Announcement

interface AnnouncementsCallback {
    fun onCallback(announcements: List<Announcement>)
}