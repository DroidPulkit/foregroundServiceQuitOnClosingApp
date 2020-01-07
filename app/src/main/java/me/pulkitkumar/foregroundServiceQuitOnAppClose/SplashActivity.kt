package me.pulkitkumar.foregroundServiceQuitOnAppClose

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Also as this is the 1st entry point into the app, we are gonna start the service over here only
        val intent = Intent(this, ExampleService::class.java)
        intent.putExtra("keepAlive", 1)
        //Here we are stating the foreground service no matter what OS it is
        ContextCompat.startForegroundService(this, intent)

        //Moving to main screen after 5 sec
        Handler().postDelayed( Runnable {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}
