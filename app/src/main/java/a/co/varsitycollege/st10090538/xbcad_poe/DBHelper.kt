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

                        val group = Group(groupID, groupName, creationDate, projectID)
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

    fun getGroupMessages(groupID: Int): Thread {
        return Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connection = DriverManager.getConnection(connectionString)

                if (connection != null) {
                    val query = "SELECT ID, UserID, MessageText FROM GroupChatMessages WHERE GroupID = $groupID"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                    val resultSet = preparedStatement.executeQuery()

                    while (resultSet.next()) {
                        val id = resultSet.getInt("ID")
                        val userid = resultSet.getInt("UserID")
                        val text = resultSet.getString("MessageText")

                        val message = GroupChatMessage(id, groupID, userid, text)
                        GlobalData.groupMessageList += message
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
    fun addGroup(groupName: String): Thread {
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

}