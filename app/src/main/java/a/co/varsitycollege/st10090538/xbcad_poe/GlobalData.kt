package a.co.varsitycollege.st10090538.xbcad_poe

import java.sql.Connection

object GlobalData {
    var connection: Connection? = null
    var loggedInUser: User? = null;
}