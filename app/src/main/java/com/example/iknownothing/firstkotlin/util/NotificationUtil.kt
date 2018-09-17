package com.example.iknownothing.firstkotlin.util

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.example.iknownothing.firstkotlin.AppConstants
import com.example.iknownothing.firstkotlin.MainActivity
import com.example.iknownothing.firstkotlin.R
import com.example.iknownothing.firstkotlin.TimerNotificationActionReciever
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtil{

companion object {
    private const val CHANNEL_ID_TIMER = "menu_timer"
    private const val CHANNEL_NAME_TIMER = "Timer App Timer"
    private const val TIME_ID = 0

    fun showTimerExpired(context : Context){
        val startIntent = Intent(context,TimerNotificationActionReciever::class.java)
        startIntent.action = AppConstants.ACTION_START
        val startPendingIntent = PendingIntent.getBroadcast(context,
                0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER,true)
        nBuilder.setContentTitle("Timer Expired!")
                .setContentText("Start Again")
                .setContentIntent(getPendingIntentWithStack(context,MainActivity::class.java))
                .addAction(R.drawable.ic_play,"Start",startPendingIntent)

        val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER,true)
        nManager.notify(TIME_ID,nBuilder.build())
    }

    fun showTimerRunning(context : Context,wakeUpTime: Long){
        val stopIntent = Intent(context,TimerNotificationActionReciever::class.java)
        stopIntent.action = AppConstants.ACTION_STOP
        val stopPendingIntent = PendingIntent.getBroadcast(context,
                0,stopIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseIntent = Intent(context,TimerNotificationActionReciever::class.java)
        pauseIntent.action = AppConstants.ACTION_PAUSE
        val pausePendingIntent = PendingIntent.getBroadcast(context,
                0,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
        val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER,true)
        nBuilder.setContentTitle("Timer is Running")
                .setContentText("End: ${df.format(Date(wakeUpTime))}")
                .setContentIntent(getPendingIntentWithStack(context,MainActivity::class.java))
                .setOngoing(true)
                .addAction(R.drawable.ic_stop,"Stop",stopPendingIntent)
                .addAction(R.drawable.ic_pause,"Pause",pausePendingIntent)

        val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER,true)
        nManager.notify(TIME_ID,nBuilder.build())
    }


    fun showTimerPaused(context : Context){
        val resumeIntent = Intent(context,TimerNotificationActionReciever::class.java)
        resumeIntent.action = AppConstants.ACTION_RESUME
        val resumePendingIntent = PendingIntent.getBroadcast(context,
                0,resumeIntent,PendingIntent.FLAG_UPDATE_CURRENT)


        val df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
        val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER,true)
        nBuilder.setContentTitle("Timer is Paused.")
                .setContentText("Resume?")
                .setContentIntent(getPendingIntentWithStack(context,MainActivity::class.java))
                .setOngoing(true)
                .addAction(R.drawable.ic_play,"Resume",resumePendingIntent)

        val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER,true)
        nManager.notify(TIME_ID,nBuilder.build())
    }

    fun hideTimerNotification(context: Context){
        val nManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager.cancel(TIME_ID)
    }

    private fun getBasicNotificationBuilder(context: Context,chanelId: String ,playSound: Boolean)
            :NotificationCompat.Builder{
        val notificationSound:Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val nBuilder = NotificationCompat.Builder(context,chanelId)
                .setSmallIcon(R.drawable.ic_timer)
                .setAutoCancel(true)
                .setDefaults(0)
         if(playSound) nBuilder.setSound(notificationSound)
        return nBuilder
    }

    private fun <T> getPendingIntentWithStack(context:Context, javaClass : Class<T>):PendingIntent{
        val resultIntent = Intent(context,javaClass)
        resultIntent.flags  = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(javaClass)
        stackBuilder.addNextIntent(resultIntent)

        return  stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @TargetApi(26)
    private fun NotificationManager.createNotificationChannel(channelId: String,
                                                              channelName: String,
                                                              playSound: Boolean){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelImportance = if (playSound) NotificationManager.IMPORTANCE_DEFAULT
            else NotificationManager.IMPORTANCE_LOW
            val nChannel = NotificationChannel(channelId,channelName,channelImportance)
                nChannel.enableLights(true)
                nChannel.lightColor= Color.BLUE
            this.createNotificationChannel(nChannel)

        }
    }
}
}