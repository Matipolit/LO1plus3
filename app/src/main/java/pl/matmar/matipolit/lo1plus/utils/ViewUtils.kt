package pl.matmar.matipolit.lo1plus.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar


const val GRADES_SPAN = 4
const val INSET_TYPE_KEY = "inset_type"
const val FULL_BLEED = "full_bleed"
const val INSET = "inset"

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.getScreenWidth(): Float{
    val displayMetrics: DisplayMetrics = this.getResources().getDisplayMetrics()
    val dpHeight = displayMetrics.heightPixels / displayMetrics.density
    return displayMetrics.widthPixels / displayMetrics.density
}

fun dpToPx(dp: Float, context: Context): Float {
    val metrics = context.resources.displayMetrics
    val fpixels = metrics.density * dp
    return (fpixels + 0.5f)
}

fun ProgressBar.show(){
    visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    visibility = View.GONE
}

fun View.snackbar(message: String, showButton:Boolean = true, buttonPrompt:String = "OK", bottomMargin: Int? = null){
    val view = this
    if(this.isAttachedToWindow){
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).apply {
            if(showButton){
                this.setAction(buttonPrompt){
                    this.dismiss()
                }
            }
            bottomMargin?.let {
                this.view.layoutParams = (this.view.layoutParams as FrameLayout.LayoutParams)
                    .apply {setMargins(leftMargin, topMargin, rightMargin, it)}
            }

        }.show()
    }
}