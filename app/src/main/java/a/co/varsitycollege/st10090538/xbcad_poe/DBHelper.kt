package a.co.varsitycollege.st10090538.xbcad_poe

import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread


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


}