package a.co.varsitycollege.st10090538.xbcad_poe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val registerText = findViewById<TextView>(R.id.registerText)

        registerText.setOnClickListener {
            setContentView(R.layout.register)
        }
    }
}