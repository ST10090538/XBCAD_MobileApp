package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Announcement
import Models.Group
import Models.GroupChatMessage
import Models.Project
import Models.StudentGroup
import Models.User
import Tools.Encryption
import android.os.Build
import androidx.annotation.RequiresApi
import net.sourceforge.jtds.jdbc.DateTime
import java.sql.Date
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
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
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connection = DriverManager.getConnection(connectionString)

                if (connection != null) {
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

                    resultSet.close()
                    preparedStatement.close()
                    connection.close()
                }
            } catch (e: Exception) {
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

    fun getAnnouncements(): Thread {
        return Thread {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connection = DriverManager.getConnection(connectionString)

                if (connection != null) {
                    val query = "SELECT * FROM Announcements"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
                    val resultSet = preparedStatement.executeQuery()

                    while (resultSet.next()) {
                        val id = resultSet.getInt("Id")
                        val username = resultSet.getString("Username")
                        val title = resultSet.getString("Title")
                        val content = resultSet.getString("Content")
                        val date = resultSet.getDate("Date")

                        val announcement = Announcement(id, username, title, content, date)
                        GlobalData.announcementList += announcement
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