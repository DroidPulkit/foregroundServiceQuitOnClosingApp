package me.pulkitkumar.foregroundServiceQuitOnAppClose

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Registering a broadcast receiver
        registerReceiver(broadcastReceiver, IntentFilter("finishActivity"))
    }

    //Setting up broadcast receiver for the service to call this activity to kill this
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            finishAndRemoveTask()
        }
    }

    //This ensures that if the user presses back button, the app first stops the service, and then kill this activity and then remove the task from recent activity menu
    override fun onBackPressed() {
        val intent = Intent(this, ExampleService::class.java)
        intent.action = ExampleService.ACTION_STOP
        startService(intent)
        //super.onBackPressed()
    }

    //Here we will unregister the broadcast receiver
    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }
}
