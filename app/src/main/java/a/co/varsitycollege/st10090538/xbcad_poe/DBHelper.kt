package a.co.varsitycollege.st10090538.xbcad_poe

import Models.Group
import Models.Project
import Models.User
import Tools.Encryption
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException


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

}