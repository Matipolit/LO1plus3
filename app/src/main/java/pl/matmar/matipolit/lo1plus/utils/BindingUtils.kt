package pl.matmar.matipolit.lo1plus.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.domain.Grade
import pl.matmar.matipolit.lo1plus.domain.Grades

@BindingAdapter("godziny")
fun TextView.setCountdown(godziny: Godziny) {
}

@BindingAdapter("godzinyVisibility")
fun View.setGodzinyVisibility(any: Any?) {
    any?.let {
        visibility = View.VISIBLE
    } ?: run {visibility = View.GONE}
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
        if(grade.opis.length <=11 ){
            text = grade.opis
        }else{
            text = "${grade.opis.substring(0, 10)}..."
        }
    }
}

@BindingAdapter("gradeColor")
fun MaterialCardView.setCardColor(grade: Grade?) {
    grade?.let {
        var bgColor: Int
        when(it.ocena){
            "1", "1+" -> bgColor = resources.getColor(R.color.color1)
            "2", "2-", "2+" -> bgColor = resources.getColor(R.color.color2)
            "3", "3-", "3+" -> bgColor = resources.getColor(R.color.color3)
            "4", "4-", "4+" -> bgColor = resources.getColor(R.color.color4)
            "5", "5-", "5+" -> bgColor = resources.getColor(R.color.color5)
            "6", "6-" -> bgColor = resources.getColor(R.color.color6)
            else -> bgColor = resources.getColor(R.color.colorDefaultGrade)
        }
        setCardBackgroundColor(bgColor)
    }
}

@BindingAdapter("roundedGradesAvarage")
fun TextView.setRoundedGradesAverage(grades: Grades?) {
    grades?.let {
        text = "%.2f".format(grades.averageVal)
    }
}