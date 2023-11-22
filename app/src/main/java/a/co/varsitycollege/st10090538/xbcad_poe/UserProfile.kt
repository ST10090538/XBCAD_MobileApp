package a.co.varsitycollege.st10090538.xbcad_poe


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL


class UserProfile : AppCompatActivity() {

    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val REQUEST_IMAGE_CAPTURE = 2
        const val CAMERA_PERMISSION_REQUEST_CODE = 3
    }
    private var imgPicture: Bitmap? = null
    private val connectionString = "DefaultEndpointsProtocol=https;AccountName=wilconnectfilestore;AccountKey=oaXtSPelTqoLjQVdjfmhgoNIe/VkQ2F2/Ov+2LqWtOspU6ollrb6u0jgoT9iJSFx+BHYBg4yn/MX+AStLGuQxg==;EndpointSuffix=core.windows.net"

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)

        GlobalScope.launch {
            downloadAndSetProfilePicture()
        }

        findViewById<TextView>(R.id.usernameTV).text = GlobalData.loggedInUser!!.userName
        findViewById<TextView>(R.id.emailTV).text = GlobalData.loggedInUser!!.email

        var userTypeTextView = findViewById<TextView>(R.id.userRoleTV)
        val userType = GlobalData.loggedInUser!!.userType
        var profilePic = findViewById<ImageView>(R.id.imgProfilePic)

        if(userType == 0){
            userTypeTextView.text = "Lecturer"
        }
        else if(userType == 1){
            userTypeTextView.text = "Student"
        }
        else if(userType == 2){
            userTypeTextView.text = "NPO"
        }


        profilePic.setOnClickListener(){
            showPictureDialog()
        }

        val changePassword = findViewById<Button>(R.id.changePasswordBtn)
        changePassword.setOnClickListener() {
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }

        val logout = findViewById<Button>(R.id.logoutBtn)
        logout.setOnClickListener() {
            GlobalData.loggedInUser = null
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    suspend fun downloadBlob(containerName: String, blobName: String): Bitmap {
        val sasToken = "?sv=2022-11-02&ss=bfqt&srt=sco&sp=rwdlacupiytfx&se=2024-01-30T02:24:55Z&st=2023-11-22T18:24:55Z&spr=https&sig=UUXYPmoc8zAIi9Z3wTEPuxPAyk8%2FCbv6wbOSVszSXmw%3D"
        val urlString = "https://wilconnectfilestore.blob.core.windows.net/$containerName/$blobName$sasToken"

        return withContext(Dispatchers.IO) {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "GET"
                val inputStream = BufferedInputStream(connection.inputStream)
                val blobData = ByteArrayOutputStream()

                inputStream.use { input ->
                    blobData.use { output ->
                        input.copyTo(output)
                    }
                }

                connection.disconnect()
                BitmapFactory.decodeByteArray(blobData.toByteArray(), 0, blobData.size())

            } finally {
                connection.disconnect()
            }
        }
    }

    // ... (other parts of your code)

    suspend fun downloadAndSetProfilePicture() {
        val containerName = "wilconnectfiles"
        val blobName = "${GlobalData.loggedInUser?.userID}_ProfilePicture.jpg"

        try {
            val bitmap = downloadBlob(containerName, blobName)

            withContext(Dispatchers.Main) {
                // Update the UI on the main thread
                findViewById<ImageView>(R.id.imgProfilePic).setImageBitmap(bitmap)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Set a default image in case of an error
            withContext(Dispatchers.Main) {
                findViewById<ImageView>(R.id.imgProfilePic).setImageResource(R.drawable.user2)
            }
        }
    }



    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }
    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }
    private fun takePhotoFromCamera() {
        // Check camera permission
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, proceed with camera intent
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, proceed with camera intent
                takePhotoFromCamera()
            } else {
                // Camera permission denied, show an error message or handle it accordingly
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val selectedImage = data?.data
                    imgPicture = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                    updateImageIcon()
                    GlobalScope.launch {
                        uploadImageToBlobStorage(imgPicture)
                    }
                }

                REQUEST_IMAGE_CAPTURE -> {
                    imgPicture = data?.extras?.get("data") as Bitmap
                    updateImageIcon()
                    GlobalScope.launch {
                        uploadImageToBlobStorage(imgPicture)
                    }
                }
            }
        }
    }


    suspend fun uploadImageToBlobStorage(imageBitmap: Bitmap?) {
        if (imageBitmap != null) {
            withContext(Dispatchers.IO) {
                try {
                    val containerName = "wilconnectfiles"
                    val blobName = "${GlobalData.loggedInUser?.userID}_ProfilePicture.jpg"
                    val sasToken = "?sv=2022-11-02&ss=bfqt&srt=sco&sp=rwdlacupiytfx&se=2024-01-30T02:24:55Z&st=2023-11-22T18:24:55Z&spr=https&sig=UUXYPmoc8zAIi9Z3wTEPuxPAyk8%2FCbv6wbOSVszSXmw%3D"
                    val urlString = "https://wilconnectfilestore.blob.core.windows.net/$containerName/$blobName$sasToken"

                    val url = URL(urlString)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "PUT"
                    connection.setRequestProperty("Content-Type", "image/jpeg")
                    connection.setRequestProperty("x-ms-blob-type", "BlockBlob")
                    connection.setRequestProperty("x-ms-blob-content-type", "image/jpeg")
                    connection.doOutput = true

                    val stream = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val imageData = stream.toByteArray()

                    connection.outputStream.use { output ->
                        ByteArrayInputStream(imageData).copyTo(output)
                    }

                    if (connection.responseCode == HttpURLConnection.HTTP_CREATED) {
                        connection.disconnect()
                    } else {
                        println("Error uploading image. Response code: ${connection.responseCode}, Response message: ${connection.responseMessage}")
                        var result = connection.responseMessage.toString()
                        var set = 1;
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun updateImageIcon() {
        val addPictureButton = findViewById<ImageView>(R.id.imgProfilePic)
        addPictureButton.setImageBitmap(imgPicture)

    }
}