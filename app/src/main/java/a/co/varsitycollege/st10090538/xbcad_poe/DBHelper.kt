package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Announcement
import Models.Group
import Models.GroupChatMessage
import Models.Project
import Models.StudentGroup
import Models.User
import Tools.Encryption
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import net.sourceforge.jtds.jdbc.DateTime
import java.sql.Date
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatterBuilder


class DBHelper {
    private val connectionString: String = "jdbc:jtds:sqlserver://wilmanagementserver.database.windows.net:1433;DatabaseName=wilManagementDB;user=st10084961@wilmanagementserver;password=Gatedark75;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;ssl=require"

    fun addUser(uname: String, email: String, password: String, intRole: Int): Thread {
        return Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                GlobalData.connection = DriverManager.getConnection(connectionString)
                val connection = GlobalData.connection

                if(connection != null)
                {
                    val query =
                        "EXEC CreateUser @Username = ?, @Email = ?, @Password = ?, @UserType = ?"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, uname)
                    preparedStatement.setString(2, email)
                    preparedStatement.setString(3, Encryption.encryptData(password))
                    preparedStatement.setInt(4, intRole)

                    preparedStatement.executeUpdate()
                }
                true

            } catch (e: Exception) {
                e.printStackTrace()
                false
            }catch (ex: SQLException){
                ex.printStackTrace()
            }
        }
    }

    fun loginUser(uname:String, password:String): Thread{
        return Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connection = DriverManager.getConnection(connectionString)
                GlobalData.connection = connection
                if (connection != null) {
                    val query = "SELECT UserID, Username, Email, UserType FROM Users WHERE Username = '$uname' AND Password = '${Encryption.encryptData(password)}'"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                    val resultSet = preparedStatement.executeQuery()

                    if (resultSet.next()) {
                        val userID = resultSet.getInt("UserID")
                        val username = resultSet.getString("Username")
                        val email = resultSet.getString("Email")
                        val userType = resultSet.getString("UserType").toInt()

                        GlobalData.loggedInUser = User(userID, username, email, userType)
                    }

                    resultSet.close()
                    preparedStatement.close()
                    connection.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getGroups(): Thread {
        return Thread {
            try {
                Log.d("DBHelper", "Attempting to connect to database...")
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connection = DriverManager.getConnection(connectionString)

                if (connection != null) {
                    Log.d("DBHelper", "Connected to database successfully.")
                    val query = "SELECT GroupID, GroupName, CreationDate, ProjectID FROM Groups"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                    val resultSet = preparedStatement.executeQuery()

                    while (resultSet.next()) {
                        val groupID = resultSet.getInt("GroupID")
                        val groupName = resultSet.getString("GroupName")
                        val creationDate = resultSet.getDate("CreationDate")
                        val projectID = resultSet.getString("ProjectID").toInt()

                        // Fetch the project name and the list of students for the group
                        val projectName = getProjectName(projectID)
                        val students = getStudentsByGroupId(groupID)

                        val group = Group(groupID, groupName, creationDate, projectID, projectName, students)
                        GlobalData.groupList += group
                    }

                    Log.d("DBHelper", "Retrieved ${GlobalData.groupList.size} groups from database.")

                    resultSet.close()
                    preparedStatement.close()
                    connection.close()
                } else {
                    Log.d("DBHelper", "Failed to connect to database.")
                }
            } catch (e: Exception) {
                Log.e("DBHelper", "An error occurred: ${e.message}", e)
                e.printStackTrace()
            }
        }
    }


    fun getProjectName(projectID: Int): String {
        var projectName = ""

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            val connection = DriverManager.getConnection(connectionString)

            if (connection != null) {
                val query = "SELECT ProjectName FROM Projects WHERE ProjectID = ?"
                val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                preparedStatement.setInt(1, projectID)
                val resultSet = preparedStatement.executeQuery()

                if (resultSet.next()) {
                    projectName = resultSet.getString("ProjectName")
                }

                resultSet.close()
                preparedStatement.close()
                connection.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return projectName
    }

    fun getProjects(): Thread {
        return Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connection = DriverManager.getConnection(connectionString)

                if (connection != null) {
                    val query = "SELECT * FROM Projects"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                    val resultSet = preparedStatement.executeQuery()

                    while (resultSet.next()) {
                        val projectID = resultSet.getInt("ProjectID")
                        val projectName = resultSet.getString("ProjectName")
                        val npoNeeds = resultSet.getString("NPONeedsDescription")
                        val projectStatus = resultSet.getInt("ProjectStatus")
                        val assignedDate = resultSet.getDate("AssignedDate")
                        val completionDate = resultSet.getDate("CompletionDate")
                        val userID = resultSet.getInt("UserID")

                        val project = Project(projectID, projectName, npoNeeds, projectStatus, assignedDate, completionDate, userID)
                        GlobalData.projectsList += project
                    }

                    resultSet.close()
                    preparedStatement.close()
                    connection.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun saveAnnouncement(username: String, title: String, content: String, date: Date) {
        Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connection = DriverManager.getConnection(connectionString)

                if (connection != null) {
                    val query = "INSERT INTO Announcements (Username, Title, Content, Date) VALUES (?, ?, ?, ?)"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, username)
                    preparedStatement.setString(2, title)
                    preparedStatement.setString(3, content)
                    preparedStatement.setDate(4, java.sql.Date(date.time))

                    preparedStatement.executeUpdate()

                    preparedStatement.close()
                    connection.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun getAnnouncements(callback: AnnouncementsCallback): List<Announcement> {
        val announcements = mutableListOf<Announcement>()
        Thread {
            try {
                Log.d("Database", "Connecting to the database...")
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connection = DriverManager.getConnection(connectionString)
                Log.d("Database", "Connection successful!")

                if (connection != null) {
                    val query = "SELECT * FROM Announcements"
                    val statement: Statement = connection.createStatement()
                    val resultSet = statement.executeQuery(query)

                    Log.d("Database", "Query executed, processing results...")
                    while (resultSet.next()) {
                        val id = resultSet.getInt("id")
                        val username = resultSet.getString("Username")
                        val title = resultSet.getString("Title")
                        val content = resultSet.getString("Content")
                        val date = resultSet.getDate("Date")

                        announcements.add(Announcement(id, username, title, content, date))
                        Log.d("Database", "Added announcement with ID: $id")
                        callback.onCallback(announcements)
                    }

                    resultSet.close()
                    statement.close()
                    connection.close()
                    Log.d("Database", "Database connection closed.")
                }
            } catch (e: Exception) {
                Log.e("Database", "An error occurred: ${e.message}")
                e.printStackTrace()
            }
        }.start()

        return announcements
    }

    fun getGroupMessages(groupID: Int): List<GroupChatMessage> {
        val groupChatMessages = mutableListOf<GroupChatMessage>()

        try {
            Log.d("getGroupMessages", "Attempting to get group messages for group ID: $groupID")
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            val connection = DriverManager.getConnection(connectionString)

            if (connection != null) {
                Log.d("getGroupMessages", "Successfully established connection")
                val query = "SELECT ID, UserID, MessageText, Timestamp FROM GroupChatMessages WHERE GroupID = $groupID"
                val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                val resultSet = preparedStatement.executeQuery()

                while (resultSet.next()) {
                    val id = resultSet.getInt("ID")
                    val userID = resultSet.getInt("UserID")
                    val text = resultSet.getString("MessageText")
                    val timestamp = resultSet.getTimestamp("Timestamp")

                    // Use getUsername() to retrieve the username for the userID
                    val username = getUsername(userID)

                    val message = GroupChatMessage(id, groupID, userID, username, text, timestamp)
                    groupChatMessages.add(message)
                }
                Log.d("getGroupMessages", "Retrieved ${groupChatMessages.size} messages")
                resultSet.close()
                preparedStatement.close()
                connection.close()
            }
        } catch (e: Exception) {
            Log.e("getGroupMessages", "Error occurred: ${e.message}")
            e.printStackTrace()
        }

        return groupChatMessages
    }

    fun saveGroupMessage(groupID: Int, senderUserID: Int, messageText: String, timestamp: java.util.Date) {
        Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connection = DriverManager.getConnection(connectionString)
                val query = "INSERT INTO GroupChatMessages (GroupID, UserID, MessageText, Timestamp) VALUES (?, ?, ?, ?)"

                val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                preparedStatement.setInt(1, groupID)
                preparedStatement.setInt(2, senderUserID)
                preparedStatement.setString(3, messageText)
                preparedStatement.setTimestamp(4, Timestamp(timestamp.time))

                preparedStatement.executeUpdate()

                preparedStatement.close()
                connection.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun saveChatMessage(senderUserID: Int, receiverUserID: Int, messageText: String, timestamp: java.util.Date) {
        Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connection = DriverManager.getConnection(connectionString)
                val query = "INSERT INTO ChatMessages (SenderID, ReceiverID, MessageText, Timestamp) VALUES (?, ?, ?, ?)"

                val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                preparedStatement.setInt(1, senderUserID)
                preparedStatement.setInt(2, receiverUserID)
                preparedStatement.setString(3, messageText)
                preparedStatement.setTimestamp(4, Timestamp(timestamp.time))

                preparedStatement.executeUpdate()

                preparedStatement.close()
                connection.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun insertChatMessage(senderUserID: Int, receiverUserID: Int, messageText: String) {
        saveChatMessage(senderUserID, receiverUserID, messageText, java.util.Date())
    }


    fun getUser(userID: Int): User? {
        var user: User? = null

        Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connection = DriverManager.getConnection(connectionString)
                val query = "SELECT Username, Email, UserType FROM Users WHERE UserID = ?"

                val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                preparedStatement.setInt(1, userID)

                val resultSet = preparedStatement.executeQuery()

                if (resultSet.next()) {
                    val username = resultSet.getString("Username")
                    val email = resultSet.getString("Email")
                    val userType = resultSet.getInt("UserType")

                    user = User(userID, username, email, userType)
                }

                resultSet.close()
                preparedStatement.close()
                connection.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()

        return user
    }

    fun getUsername(userID: Int): String {
        var username = ""
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            val connection = DriverManager.getConnection(connectionString)

            if (connection != null) {
                val query = "SELECT Username FROM Users WHERE UserID = $userID"
                val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                val resultSet = preparedStatement.executeQuery()

                if (resultSet.next()) {
                    username = resultSet.getString("Username")
                }

                resultSet.close()
                preparedStatement.close()
                connection.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return username
    }




    fun getStudentGroups(): Thread {
        return Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connection = DriverManager.getConnection(connectionString)

                if (connection != null) {
                    val query = "SELECT * FROM StudentGroups"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                    val resultSet = preparedStatement.executeQuery()

                    while (resultSet.next()) {
                        val id = resultSet.getInt("StudentGroupID")
                        val groupID = resultSet.getInt("GroupID")
                        val studentID = resultSet.getInt("StudentID")
                        val date = resultSet.getDate("JoinDate")

                        val studentGroup = StudentGroup(id, groupID, studentID, date)
                        GlobalData.studentGroupList += studentGroup
                    }

                    resultSet.close()
                    preparedStatement.close()
                    connection.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addGroup(groupName: String, callback: (Boolean) -> Unit): Thread {
        return Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                GlobalData.connection = DriverManager.getConnection(connectionString)
                val connection = GlobalData.connection

                if(connection != null)
                {
                    val currentDateTime = LocalDateTime.now()
                    val currentDate = Date.valueOf(currentDateTime.toLocalDate().toString())

                    val query =
                        "EXEC CreateGroup @GroupName = ?, @CreationDate = ?, @ProjectID = 0, @LecturerID = 0"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, groupName)
                    preparedStatement.setDate(2,currentDate )

                    preparedStatement.executeUpdate()
                    preparedStatement.close()
                    connection.close()
                }

                true

            } catch (e: Exception) {
                e.printStackTrace()
                false
            }catch (ex: SQLException){
                ex.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun enrollStudentInGroup(groupID: Int, userID: Int): Thread {
        return Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                GlobalData.connection = DriverManager.getConnection(connectionString)
                val connection = GlobalData.connection

                if(connection != null)
                {
                    val currentDateTime = LocalDateTime.now()
                    val currentDate = Date.valueOf(currentDateTime.toLocalDate().toString())

                    val query =
                        "EXEC EnrollStudentInGroup @GroupID = ?, @StudentID = ?, @JoinDate = ?"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                    preparedStatement.setInt(1, groupID)
                    preparedStatement.setInt(2, userID )
                    preparedStatement.setDate(3, currentDate )

                    preparedStatement.executeUpdate()
                    preparedStatement.close()
                    connection.close()
                }

                true

            } catch (e: Exception) {
                e.printStackTrace()
                false
            }catch (ex: SQLException){
                ex.printStackTrace()
            }
        }
    }

    fun createProject(projectName: String, needsDesc: String, userID: Int): Thread {
        return Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                GlobalData.connection = DriverManager.getConnection(connectionString)
                val connection = GlobalData.connection

                if(connection != null)
                {
                    val query =
                        "EXEC CreateProject @ProjectName = ?, @NPONeedsDescription = ?, @ProjectStatus = ?, @UserID = ?"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, projectName)
                    preparedStatement.setString(2, needsDesc )
                    preparedStatement.setInt(3, 0 )
                    preparedStatement.setInt(4, userID )

                    preparedStatement.executeUpdate()
                    preparedStatement.close()
                    connection.close()
                }

                true

            } catch (e: Exception) {
                e.printStackTrace()
                false
            }catch (ex: SQLException){
                ex.printStackTrace()
            }
        }
    }

    fun assignProject(groupID: Int, projectID: Int): Thread {
        return Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                GlobalData.connection = DriverManager.getConnection(connectionString)
                val connection = GlobalData.connection

                if(connection != null)
                {
                    val query =
                        "EXEC AssignProjectToGroup @GroupID = ?, @ProjectID = ?"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                    preparedStatement.setInt(1, groupID )
                    preparedStatement.setInt(2, projectID )

                    preparedStatement.executeUpdate()
                    preparedStatement.close()
                    connection.close()
                }

                true

            } catch (e: Exception) {
                e.printStackTrace()
                false
            }catch (ex: SQLException){
                ex.printStackTrace()
            }
        }
    }

    fun getStudentsWithoutGroups(students: List<User>): Thread {
        return Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connection = DriverManager.getConnection(connectionString)

                if (connection != null) {
                    for (student in students) {
                        val query = "SELECT * FROM StudentGroups WHERE StudentID = ?"
                        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                        preparedStatement.setInt(1, student.userID)
                        val resultSet = preparedStatement.executeQuery()

                        if (!resultSet.next()) {
                            GlobalData.studentsWithoutGroupsList.add(student)
                        }

                        resultSet.close()
                        preparedStatement.close()
                    }
                    connection.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAllStudents(): List<User> {
        val students = mutableListOf<User>()

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            val connection = DriverManager.getConnection(connectionString)
            val query = "SELECT * FROM Users WHERE UserType = 1"
            val preparedStatement: PreparedStatement = connection.prepareStatement(query)
            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                val user = User(
                    userID = resultSet.getInt("UserID"),
                    userName = resultSet.getString("Username"),
                    email = resultSet.getString("Email"),
                    userType = resultSet.getInt("UserType")
                )
                students.add(user)
            }

            resultSet.close()
            preparedStatement.close()
            connection.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return students
    }

    fun getStudentsByGroupId(groupID: Int): List<User> {
        val students = mutableListOf<User>()

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            val connection = DriverManager.getConnection(connectionString)

            if (connection != null) {
                val query = "SELECT StudentID FROM StudentGroups WHERE GroupID = ?"
                val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                preparedStatement.setInt(1, groupID)
                val resultSet = preparedStatement.executeQuery()

                while (resultSet.next()) {
                    val studentID = resultSet.getInt("StudentID")
                    val username = getUsername(studentID)

                    val student = User(
                        userID = studentID,
                        userName = username ?: "",
                        email = "",
                        userType = 1
                    )
                    students += student
                }

                resultSet.close()
                preparedStatement.close()
                connection.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return students
    }

}