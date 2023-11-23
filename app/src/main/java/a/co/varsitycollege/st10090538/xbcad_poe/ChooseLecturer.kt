package a.co.varsitycollege.st10090538.xbcad_poe

import Models.User
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors



class ChooseLecturer : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_lecturer)

        recyclerView = findViewById(R.id.chooselecturerrecycle)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DBHelper()

        dbHelper.getLecturers(object : LecturersCallback {
            override fun onCallback(lecturers: List<Lecturer>) {
                runOnUiThread {
                    val adapter = LecturerAdapter(lecturers) { lecturer ->
                        // Handle click event here
                    }
                    recyclerView.adapter = adapter
                }
            }
        })
    }
}




