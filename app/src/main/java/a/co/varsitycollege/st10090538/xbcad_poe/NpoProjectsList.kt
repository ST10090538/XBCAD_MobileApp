package a.co.varsitycollege.st10090538.xbcad_poe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class NpoProjectsList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.npo_projects_list)

        val helper = DBHelper().getProjects()
        helper.start()
        helper.join()


    }
}