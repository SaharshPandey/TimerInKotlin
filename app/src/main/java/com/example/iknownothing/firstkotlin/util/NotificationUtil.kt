package com.example.iknownothing.firstkotlin.util

import android.content.Context
import android.content.Intent
import com.example.iknownothing.firstkotlin.AppConstants
import com.example.iknownothing.firstkotlin.TimerNotificationActionReciever

class NotificationUtil{

companion object {
    private const val CHANNEL_ID_TIMER = "menu_timer"
    private const val CHANNEL_NAME_TIMER = "Timer App Timer"
    private const val TIME_ID = 0

    fun showTimerExpired(context : Context){
        val startIntent = Intent(context,TimerNotificationActionReciever::class.java)
        startIntent.action = AppConstants.ACTION_START


    }


}
}