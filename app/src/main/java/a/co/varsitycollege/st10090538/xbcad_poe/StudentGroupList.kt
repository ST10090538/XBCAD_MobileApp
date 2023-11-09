package a.co.varsitycollege.st10090538.xbcad_poe

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class StudentGroupList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_group_list)

        val helper = DBHelper().getGroups()
        helper.start()
        helper.join()

    }
}


