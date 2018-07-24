package com.gmail.alhadiq.zamrudi.timerlockscreen.lock_services

import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), View.OnClickListener {
    //init variable
    private var timeCountInMilliSeconds = (1 * 60000).toLong()
    private enum class TimerStatus {
        STARTED,
        STOPPED
    }
    private var timerStatus = TimerStatus.STOPPED

    private var progressBarCircle: ProgressBar? = null
    private var editTextMinute: EditText? = null
    private var editTextSecond: EditText? = null
    private var textViewTime: TextView? = null
    private var imageViewReset: ImageView? = null
    private var imageViewStartStop: ImageView? = null
    private var countDownTimer: CountDownTimer? = null
    private var checkBox: CheckBox?=null
    //var for device admin
    private var devicePolicyManager: DevicePolicyManager? = null
    private var activityManager: ActivityManager? =null
    private var compName: ComponentName?=null
    val REQUEST_CODE_ENABLE_ADMIN = 1
    protected var mAdminActive: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //init device policy
        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        compName = ComponentName(this, AdminLock::class.java)


        initEnableAdmin()
        initViews()
        initListeners()


    }

    private fun initEnableAdmin() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "This Permission will make app to be able to lock from admin")
        startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN)
    }


    private fun initViews() {
        progressBarCircle = findViewById(R.id.progressBarCircle) as ProgressBar
        editTextMinute = findViewById(R.id.editTextMinute) as EditText
        editTextSecond = findViewById(R.id.editTextSecond) as EditText
        textViewTime = findViewById(R.id.textViewTime) as TextView
        imageViewReset = findViewById(R.id.imageViewReset) as ImageView
        imageViewStartStop = findViewById(R.id.imageViewStartStop) as ImageView

    }

    private fun initListeners() {
        imageViewReset!!.setOnClickListener(this)
        imageViewStartStop!!.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.imageViewReset -> reset()
            R.id.imageViewStartStop -> startStop()
        }
    }


    private fun reset() {
        stopCountDownTimer();
        startCountDownTimer();
    }

    private fun startStop() {
        if (timerStatus == TimerStatus.STOPPED) {
            setTimerValues()
            setProgressBarValues()
            imageViewReset!!.visibility = View.VISIBLE
            imageViewStartStop!!.setImageResource(R.drawable.icon_stop)
            editTextMinute!!.isEnabled = false
            timerStatus = TimerStatus.STARTED
            startCountDownTimer()
        } else {
            imageViewReset!!.visibility = View.GONE
            imageViewStartStop!!.setImageResource(R.drawable.icon_start)
            editTextMinute!!.isEnabled = true
            timerStatus = TimerStatus.STOPPED
            stopCountDownTimer()
        }
    }

    private fun setTimerValues() {
        var time = 0
        var timeSecond = 0
        if (!editTextMinute!!.text.toString().isEmpty()){
            time = editTextMinute!!.text.toString().trim().toInt()
            timeSecond = editTextSecond!!.text.toString().trim().toInt()
        } else {
            Toast.makeText(applicationContext, getString(R.string.message_minutes), Toast.LENGTH_LONG).show()
        }
        timeCountInMilliSeconds = (time * 60 * 1000).toLong() + (timeSecond * 1000).toLong()
    }

    private fun startCountDownTimer() {
        countDownTimer = object : CountDownTimer(timeCountInMilliSeconds, 1000) {
            override fun onTick(milisUntilFinished: Long) {
                textViewTime!!.text = hmsTimeFormatter(milisUntilFinished)
                progressBarCircle!!.progress = (milisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                textViewTime!!.text = hmsTimeFormatter(timeCountInMilliSeconds)
                setProgressBarValues()
                imageViewReset!!.visibility = View.GONE
                imageViewStartStop!!.setImageResource(R.drawable.icon_start)
                editTextMinute!!.isEnabled = true
                timerStatus = TimerStatus.STOPPED
                devicePolicyManager!!.lockNow()

            }

        }.start()
        countDownTimer!!.start()
    }

    private fun stopCountDownTimer(){
        countDownTimer!!.cancel()
    }

    private fun setProgressBarValues() {
        progressBarCircle!!.max = (timeCountInMilliSeconds/1000).toInt()
        progressBarCircle!!.progress = (timeCountInMilliSeconds/1000).toInt()
    }

    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private fun hmsTimeFormatter(milliSeconds: Long): String {

        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)))


    }

}
