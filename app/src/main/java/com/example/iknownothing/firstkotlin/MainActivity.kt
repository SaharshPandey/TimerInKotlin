package com.example.iknownothing.firstkotlin

import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.iknownothing.firstkotlin.util.PrefUtil

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    enum class TimerState{
        Stopped,Paused,Running
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TimerState.Stopped
    private var secondsRemaining =0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_timer)
        supportActionBar?.setTitle("     Timer")

        fab_start.setOnClickListener{v->
            startTimer()
            timerState = TimerState.Running
            updaateButtons()
        }

        fab_pause.setOnClickListener{v->
            timer.cancel()
            timerState = TimerState.Paused
            updaateButtons()
        }

        fab_stop.setOnClickListener{v->
            timer.cancel()
            onTimerFinished()
            timerState = TimerState.Running
            updaateButtons()
        }

    }

    override fun onResume() {
        super.onResume()

        initTimer()

        //TODO: remove background timer, hide notification

    }

    override fun onPause() {
        super.onPause()

        if(timerState == TimerState.Running)
        {
            timer.cancel()
            //TODO: start background timer and show notification.
        }

        else if(timerState ==TimerState.Paused)
        {
            //TODO: show notification.
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds,this)
        PrefUtil.setSecondsRemaining(secondsRemaining,this)
        PrefUtil.setTimerState(timerState,this)


    }

    private fun initTimer(){
        timerState =  PrefUtil.getTimerState(this)

        if(timerState==TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if(timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(this)
        else
            timerLengthSeconds

        //TODO: change secondsRemaining according to where the background timerstopped

        //resume where we left off.
        if(timerState == TimerState.Running)
            startTimer()

        updaateButtons()
        updateCountdownUI()

    }

    private fun onTimerFinished()
    {
        timerState = TimerState.Stopped

        setNewTimerLength()

        progress_count.progress = 0
        PrefUtil.setSecondsRemaining(timerLengthSeconds,this)
        secondsRemaining = timerLengthSeconds

        updaateButtons()
        updateCountdownUI()
    }

    private fun startTimer()
    {
        timerState = TimerState.Running
        timer =  object : CountDownTimer(secondsRemaining*1000,1000){
            override fun onFinish() =onTimerFinished()
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining =  millisUntilFinished/1000
                updateCountdownUI()
            }
        }.start()

    }

    private fun setNewTimerLength(){
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        progress_count.max=timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        progress_count.max=timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining/60
        val secondsInMinuteUntilFinished = secondsRemaining -minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        textview_countdown.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr
        else "0"+secondsStr}"

        progress_count.progress= (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updaateButtons()
    {
        when(timerState){
            TimerState.Running -> {
                fab_start.isEnabled =false
                fab_pause.isEnabled =true
                fab_stop.isEnabled =true
            }
            TimerState.Paused -> {
                fab_start.isEnabled =true
                fab_pause.isEnabled =false
                fab_stop.isEnabled =true
            }
            TimerState.Stopped -> {
                fab_start.isEnabled =true
                fab_pause.isEnabled =true
                fab_stop.isEnabled =false
            }
        }
    }


    private fun onCreateOptionMenu(menu:Menu):Boolean{
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}