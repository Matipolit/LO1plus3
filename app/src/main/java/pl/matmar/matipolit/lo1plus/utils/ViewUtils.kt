package pl.matmar.matipolit.lo1plus.utils

import android.content.Context
import android.view.View
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

fun ProgressBar.show(){
    visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    visibility = View.GONE
}

fun View.snackbar(message: String, showButton:Boolean = true, buttonPrompt:String = "OK"){
    if(this.isAttachedToWindow){
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
            if(showButton){
                snackbar.setAction(buttonPrompt){
                    snackbar.dismiss()
                }
            }
        }.show()
    }
}