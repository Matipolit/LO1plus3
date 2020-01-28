package pl.matmar.matipolit.lo1plus.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import pl.matmar.matipolit.lo1plus.R
import pl.matmar.matipolit.lo1plus.domain.Grade
import pl.matmar.matipolit.lo1plus.domain.Grades
import pl.matmar.matipolit.lo1plus.domain.Lekcja
import pl.matmar.matipolit.lo1plus.domain.WeekDay
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("godziny")
fun TextView.setCountdown(godziny: Godziny) {
}

@BindingAdapter("viewVisibilityFrom")
fun View.setVisibilityFrom(any: Any?) {
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
        if(grade.opis.length <=9 ){
            text = grade.opis
        }else{
            text = "${grade.opis.substring(0, 8)}..."
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


@BindingAdapter("noYearWeekDayDate")
fun TextView.setNoYearWeekDayDate(weekDay: WeekDay?) {
    weekDay?.let {
        val splitted = it.date.split(".")
        text = "${splitted[0]}.${splitted[1]}"
    }
}

@BindingAdapter("splitLekcjaDataFirst")
fun TextView.setSplitLekcjaDataFirst(lekcja: Lekcja?) {
    lekcja?.let {
        val splitted = it.data.split("\n")
        text = splitted[0].asFormattedSpannable()
    }
}

@BindingAdapter("splitLekcjaDataSecond")
fun TextView.setSplitLekcjaDataSecond(lekcja: Lekcja?) {
    lekcja?.let {
        val data = it.data
        val splitted = data.split("\n")
        if(splitted.size > 1){
            text = data.substring(data.indexOf("\n")+1).asFormattedSpannable()
        }

    }
}

@BindingAdapter("lekcjaSecondVisibility")
fun View.setLekcjaSecondVisibility(lekcja: Lekcja?) {
    lekcja?.let {
        val splitted = it.data.split("\n")
        if(splitted.size > 1){
            visibility = View.VISIBLE
        }else{
            visibility = View.GONE
        }
    }
}

@BindingAdapter("firstLastDaysOfWeek")
fun TextView.setFirstLastDaysOfWeek(cal: Calendar?) {
    cal?.let {
        val format = SimpleDateFormat("dd.MM", Locale.US)
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val first = format.format(cal.time)
        cal.add(Calendar.DAY_OF_WEEK, 6)
        val last = format.format(cal.time)
        text = "$first - $last"
    }
}