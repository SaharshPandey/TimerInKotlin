package com.example.iknownothing.firstkotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.iknownothing.firstkotlin.util.NotificationUtil
import com.example.iknownothing.firstkotlin.util.PrefUtil

class TimerNotificationActionReciever : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

      when (intent.action){
          AppConstants.ACTION_STOP -> {
              MainActivity.removeAlarm(context)
              PrefUtil.setTimerState(MainActivity.TimerState.Stopped,context)
              NotificationUtil.hideTimerNotification(context)
          }

          AppConstants.ACTION_PAUSE -> {
              var secondsRemaining = PrefUtil.getSecondsRemaining(context)
              val alarmSetTime = PrefUtil.getAlarmSetTime(context)
              val nowSeconds = MainActivity.nowSeconds

              secondsRemaining -= nowSeconds - alarmSetTime
              PrefUtil.setSecondsRemaining(secondsRemaining,context)

              MainActivity.removeAlarm(context)
              PrefUtil.setTimerState(MainActivity.TimerState.Paused,context)
              NotificationUtil.showTimerPaused(context)
          }

          AppConstants.ACTION_RESUME -> {
              val secondsRemaining = PrefUtil.getSecondsRemaining(context)
              val wakeUpTime =MainActivity.setAlarm(context,MainActivity.nowSeconds,secondsRemaining)
              PrefUtil.setTimerState(MainActivity.TimerState.Running,context)
              NotificationUtil.showTimerRunning(context,wakeUpTime)
          }

          AppConstants.ACTION_START -> {
              val minutesRemaining = PrefUtil.getTimerLength(context)
              val secondsRemaining =minutesRemaining * 60L
              val wakeUpTime =MainActivity.setAlarm(context,MainActivity.nowSeconds,secondsRemaining)
              PrefUtil.setTimerState(MainActivity.TimerState.Running,context)
              PrefUtil.setSecondsRemaining(secondsRemaining,context)
              NotificationUtil.showTimerRunning(context,wakeUpTime)
          }

      }

        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        TODO("TimerNotificationActionReciever.onReceive() is not implemented")
    }
}
