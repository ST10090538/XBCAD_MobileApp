package a.co.varsitycollege.st10090538.xbcad_poe
import a.co.varsitycollege.st10090538.xbcad_poe.R.id.WelcomeLogo
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome)
        findViewById<ImageView>(R.id.WelcomeLogo).setOnClickListener(){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
