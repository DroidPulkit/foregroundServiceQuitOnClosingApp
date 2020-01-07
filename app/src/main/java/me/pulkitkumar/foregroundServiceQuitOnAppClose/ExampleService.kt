package me.pulkitkumar.foregroundServiceQuitOnAppClose

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import me.pulkitkumar.foregroundServiceQuitOnAppClose.App.Companion.CHANNEL_ID


//Here let's create Foreground Service, which do it's long running work on background threads
class ExampleService : Service(){

    companion object {
        var KEEP_ALIVE: Int = 0
        val ACTION_STOP = "stop"
    }

    private val TAG: String = ExampleService::class.java.simpleName
    private lateinit var handlerThread : HandlerThread
    private lateinit var handler: Handler

    //Here my objective will be as soon, as people bind to this service, we will increment a KeepAlive counter, which was initially 0,
    // This will be checked after every 10 seconds, that if that counter reaches 0, then we will stop the foreground service
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        handlerThread = HandlerThread("ServiceBackgroundHandler")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopAppAndQuit()
            return START_NOT_STICKY
        }

        //Setting up the foreground service notification
        val stopnotificationIntent = Intent(this, ExampleService::class.java)
        stopnotificationIntent.action = ACTION_STOP
        val pendingIntent: PendingIntent = PendingIntent.getService(this, 0, stopnotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification : Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Foreground service is running")
            .setSmallIcon(R.drawable.ic_android)
            .addAction(android.R.drawable.ic_media_pause, "Stop", pendingIntent)
            .build()

        startForeground(1, notification)

        doSomeBackgroundWork()

        return START_NOT_STICKY
    }

    private fun doSomeBackgroundWork(){
        handler.postDelayed( Runnable {
            Log.d(TAG, "running")
            doSomeBackgroundWork()
        } , 10000)
    }

    //This is run, from the notification, in case user is not interested in running the app
    private fun stopAppAndQuit(){
        handler.removeCallbacksAndMessages(null)
        handlerThread.quitSafely()
        stopForeground(true)
        stopSelf()
        sendBroadcast(Intent("finishActivity"))
        //val intent = Intent().setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        //startActivity(intent)
    }

    //We are overwriting this, as we want the foreground service to be killed as soon as the app is closed from the recent screen
    override fun onTaskRemoved(rootIntent: Intent?) {
        handler.removeCallbacksAndMessages(null)
        handlerThread.quitSafely()
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        handlerThread.quitSafely()
        super.onDestroy()
    }
}