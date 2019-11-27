package pl.matmar.matipolit.lo1plus.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

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
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        if(showButton){
            snackbar.setAction(buttonPrompt){
                snackbar.dismiss()
        }
        }
    }.show()
}