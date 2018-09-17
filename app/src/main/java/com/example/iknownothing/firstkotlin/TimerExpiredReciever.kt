package com.example.iknownothing.firstkotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.iknownothing.firstkotlin.util.PrefUtil

class TimerExpiredReciever : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        TODO("TimerExpiredReciever.onReceive() is not implemented")

        PrefUtil.setTimerState(MainActivity.TimerState.Stopped,context)
        PrefUtil.setAlarmSetTime(0,context)

    }
}
