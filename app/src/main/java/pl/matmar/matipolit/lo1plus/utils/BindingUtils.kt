package pl.matmar.matipolit.lo1plus.utils

import android.os.CountDownTimer
import android.widget.TextView
import androidx.databinding.BindingAdapter
import timber.log.Timber

@BindingAdapter("countdown")
fun TextView.setCountdown(godziny: Godziny) {
    kotlin.run {
        val countdownTime = (godziny.dzwonekDelay!!*1000).toLong()
        val ONE_SECOND = 1000L
        val timer: CountDownTimer
        timer = object : CountDownTimer(countdownTime, ONE_SECOND) {
            override fun onFinish() {
                text = "finish"
                Timber.d("finish")
            }

            override fun onTick(millisUntilFinished: Long) {
                text = (millisUntilFinished/ONE_SECOND).toString()
                Timber.d("tick")
            }
        }
        timer.start()
    }
}