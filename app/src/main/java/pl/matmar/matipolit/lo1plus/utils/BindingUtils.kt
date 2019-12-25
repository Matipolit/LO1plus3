package pl.matmar.matipolit.lo1plus.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import pl.matmar.matipolit.lo1plus.domain.Grade

@BindingAdapter("godziny")
fun TextView.setCountdown(godziny: Godziny) {
}

@BindingAdapter("gradeDate")
fun TextView.setGradeDate(grade: Grade?) {
    grade?.let {
        val arr = it.data.split(".")
        if(arr.size >= 2){
            text = "${arr[0]}.${arr[1]}"
        }else{
            text = "-"
        }
    }
}

@BindingAdapter("gradeWeight")
fun TextView.setGradeWeight(grade: Grade?) {
    grade?.let {
        text = grade.waga
    }
}

@BindingAdapter("gradeVal")
fun TextView.setGradeValue(grade: Grade?) {
    grade?.let {
        text = grade.ocena
    }
}

@BindingAdapter("gradeName")
fun TextView.setGradeName(grade: Grade?) {
    grade?.let {
        if(grade.opis.length <=9 ){
            text = grade.opis
        }else{
            text = "${grade.opis.substring(0, 8)}..."
        }
    }
}