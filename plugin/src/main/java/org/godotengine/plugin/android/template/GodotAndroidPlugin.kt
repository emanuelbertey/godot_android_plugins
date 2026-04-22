package org.godotengine.plugin.android.template

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.UsedByGodot

class GodotAndroidPlugin(godot: Godot) : GodotPlugin(godot) {

    override fun getPluginName() = "GodotAndroidPlugin"

    @UsedByGodot
    fun helloWorld() {
        runOnUiThread {
            Toast.makeText(activity, "Hello World", Toast.LENGTH_LONG).show()
            Log.v(pluginName, "Hello World")
        }
    }

    @UsedByGodot
    fun start_foreground_service() {
        activity?.let {
            val intent = Intent(it, GodotService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.startForegroundService(intent)
            } else {
                it.startService(intent)
            }
        } ?: Log.e(pluginName, "Activity is null, cannot start service")
    }

    @UsedByGodot
    fun call_pause() {
        activity?.runOnUiThread {
            Log.v(pluginName, "Requested pause_script (placeholder)")
            // Aquí podrías usar GodotLib.calldeferred("GameControl", "pause_script") si lo conectás vía JNI
        }
    }

    @UsedByGodot
    fun call_play() {
        activity?.runOnUiThread {
            Log.v(pluginName, "Requested play_script (placeholder)")
            // Aquí podrías usar GodotLib.calldeferred("GameControl", "play_script") si lo conectás vía JNI
        }
    }

    @UsedByGodot
    fun stop_foreground_service() {
        activity?.let {
            val intent = Intent(it, GodotService::class.java)
            it.stopService(intent)
        } ?: Log.e(pluginName, "Activity is null, cannot stop service")
    }
}

class GodotService : Service() {
    companion object {
        const val ACTION_STOP_SERVICE = "STOP_SERVICE"
    }

    override fun onCreate() {
        super.onCreate()
        val channelId = "godot_channel"
        val channel = NotificationChannel(channelId, "Godot Service", NotificationManager.IMPORTANCE_LOW)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        val stopIntent = Intent(this, GodotService::class.java).apply {
            action = ACTION_STOP_SERVICE
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 
            0, 
            stopIntent, 
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("Godot Service")
            .setContentText("Running in background")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Cerrar", stopPendingIntent)
            .build()

        startForeground(1, notification)
        Log.v("GodotService", "Servicio iniciado")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP_SERVICE) {
            Log.v("GodotService", "Deteniendo servicio desde notificación")
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
