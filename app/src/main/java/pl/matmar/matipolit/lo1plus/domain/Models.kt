package pl.matmar.matipolit.lo1plus.domain

import android.os.Parcelable
import com.xwray.groupie.Section
import kotlinx.android.parcel.Parcelize
import pl.matmar.matipolit.lo1plus.ui.attendance.overview.AttendanceDayHeaderItem
import pl.matmar.matipolit.lo1plus.ui.attendance.overview.AttendanceLessonItem
import pl.matmar.matipolit.lo1plus.ui.plan.PlanDayHeaderItem
import pl.matmar.matipolit.lo1plus.ui.plan.PlanLessonItem
import pl.matmar.matipolit.lo1plus.utils.*
import java.text.SimpleDateFormat
import java.util.*

data class HomeCard(
    val title: String,
    val content: String,
    val color: Int,
    val icon: Int
){
    val formattedContent = content.asFormattedSpannable()
    val id = title.toCardType()
}

@Parcelize
data class Grade(
    val kod: String,
    val opis: String,
    val waga: String,
    val data: String,
    val nauczyciel: String,
    val ocena: String,
    val komentarz: String?
) : Parcelable {
    val wagaInt = Character.getNumericValue(waga[0])
    var dodatek: Float = 0f
    var ocenaFloat = 0f
    var liczysie = false
    init {
        if (ocena.length == 2) {
            if (ocena[1].toString() == "+") {
                dodatek = 0.5f
            } else if (ocena[1].toString() == "-") {
                dodatek = -0.25f
            }
        }
        if (ocena != "+" && ocena != "-") {
            if (ocena.isNotEmpty()) {
                if (ocena[0].toString().matches("\\d+(?:\\.\\d+)?".toRegex())) {
                    ocenaFloat = Integer.valueOf(ocena[0].toString()) + dodatek
                    liczysie = true
                }
            } else {
                ocenaFloat = 0f
                liczysie = false
            }
        }
    }
}

data class Subject(
    val name: String,
    val oceny: ArrayList<Grade>,
    val srednia: String,
    val przewidywana_śródroczna: String,
    val ocena_śródroczna: String,
    val przewidywana_roczna: String,
    val ocena_roczna: String
){
    var sredniaFloat : Float? = null
    var sredniaText : String? = null
    var sredniaVal : String? = null
    init {
        sredniaFloat = srednia.toFloat()
        sredniaText = when{
            ocena_roczna != "-" -> "Ocena roczna"
            przewidywana_roczna != "-" -> "Ocena przewidywana roczna"
            ocena_śródroczna != "-" -> "Ocena śródroczna"
            przewidywana_śródroczna != "-" -> "Ocena przewidywana śródroczna"
            else -> null
        }
        sredniaVal = when {
            ocena_roczna != "-" -> ocena_roczna
            przewidywana_roczna != "-" -> przewidywana_roczna
            ocena_śródroczna != "-" -> ocena_śródroczna
            przewidywana_śródroczna != "-" -> przewidywana_śródroczna
            else -> null
        }
    }
}

data class Grades(
    val oceny: List<Subject>,
    val semestr: Int,
    val semestr1ID: Int,
    val klasa: String?,
    val date: Date?
){
    //val subjectAverage = oceny.map { it.sredniaFloat }.average()
    var averageText : String? = null
    var averageVal : Float? = null
    init {
        var areRoczne = false
        var arePrzewidywaneRoczne = false
        var areSrodroczne = false
        var arePrzewidywaneSrodroczne = false
        var sumaRocznych = 0F
        var sumaSrodrocznych = 0F
        var iloscRocznych = 0
        var iloscSrodrocznych = 0

        for(subject in oceny){
            if(subject.ocena_roczna != "-" || subject.przewidywana_roczna != "-"){
                iloscRocznych += 1
                if(subject.ocena_roczna != "-" && subject.ocena_roczna.isNumeric()){
                    areRoczne = true
                    sumaRocznych += subject.ocena_roczna.toFloat()
                }else if(subject.przewidywana_roczna.isNumeric()){
                    arePrzewidywaneRoczne = true
                    sumaRocznych += subject.przewidywana_roczna.toFloat()
                }
            }
            if(subject.ocena_śródroczna != "-" || subject.przewidywana_śródroczna != "-"){
                iloscSrodrocznych += 1
                if(subject.ocena_śródroczna != "-" && subject.ocena_śródroczna.isNumeric()){
                    areSrodroczne = true
                    sumaSrodrocznych += subject.ocena_śródroczna.toFloat()
                }else if(subject.przewidywana_śródroczna.isNumeric()){
                        arePrzewidywaneRoczne = true
                        sumaSrodrocznych += subject.przewidywana_śródroczna.toFloat()
                }
            }
        }
        //Timber.d("Suma rocznych: ${sumaRocznych}\nIlość rocznych: $iloscRocznych\nSuma śródrocznych: $sumaSrodrocznych\nIlość śródrocznych: $iloscSrodrocznych")
        val sredniaRocznych = sumaRocznych / iloscRocznych
        val sredniaSrodrocznych = sumaSrodrocznych / iloscSrodrocznych

        if(areRoczne && !arePrzewidywaneRoczne){
            averageVal = sredniaRocznych
            averageText = "rocznych"
        }else if(areRoczne && arePrzewidywaneRoczne){
            averageVal = sredniaRocznych
            averageText = "rocznych i przewidywanych rocznych"
        }else if(arePrzewidywaneRoczne && !areRoczne){
            averageVal = sredniaRocznych
            averageText = "przewidywanych rocznych"
        }else if(areSrodroczne && !arePrzewidywaneSrodroczne){
            averageVal = sredniaSrodrocznych
            averageText = "śródrocznych"
        }else if(areSrodroczne && arePrzewidywaneSrodroczne){
            averageVal = sredniaSrodrocznych
            averageText = "śródrocznych i przewidywanych śródrocznych"
        }else if(arePrzewidywaneSrodroczne && !areSrodroczne) {
            averageVal = sredniaSrodrocznych
            averageText = "przewidywanych śródrocznych"
        }



    }
}

data class PlanWrapper(
    val plan: Plan,
    val klasa: String?
)

class Plan(
    val godziny: List<String>,
    val tydzien: List<PlanDay>
){
    init {
        var godzinySplit = godziny.map {
            it.split(" ")
        }
        var begDate = SimpleDateFormat("dd.MM.yyyy", Locale.US).parse(tydzien.first().date)
        var endDate = SimpleDateFormat("dd.MM.yyyy", Locale.US).parse(tydzien.last().date)
    }


}

data class PlanDay(
    val name: String,
    val date: String,
    val lekcje: List<PlanLesson>
)

data class PlanLesson(
    val index: Int,
    val data: String
)

fun Plan.asSections() : List<Section>{
    return this.tydzien.map {
        Section().apply {
            setHeader(
                PlanDayHeaderItem(it)
            )
            addAll(it.lekcje.map {
                val godz = godziny[it.index-1].split(" ")
                val godzinyFormatted = "${godz[0]} - ${godz[1]}"
                PlanLessonItem(
                        it,
                        godzinyFormatted
                    )
            })
        }
    }
}

data class AttendanceWrapper(
    val attendance: Attendance,
    val klasa: String?
)

data class Attendance(
    val procent: String,
    val godziny: List<String>,
    val tydzien: List<AttDay>
){
    init {
        val godzinySplit = godziny.map {
            it.split(" ")
        }
        var begDate: Date? = null
        var endDate: Date? = null
        if(godziny.size >= 2){
            begDate = SimpleDateFormat("dd.MM.yyyy", Locale.US).parse(tydzien.first().date)
            endDate = SimpleDateFormat("dd.MM.yyyy", Locale.US).parse(tydzien.last().date)
        }

    }

}

data class AttDay(
    val name: String,
    val date: String,
    val lekcje: List<AttLesson>
)

data class AttLesson(
    val index: Int,
    val data: String,
    val type: String,
    val attType: Int = type.asAttType()
) {
    fun getPresence(): Boolean? = type.asAttType().asAttPresence()
    fun getColor(): Int = type.asAttType().asAttColorInt()
}

fun Attendance.asSections() : List<Section>{
    return this.tydzien.map {
        Section().apply {
            setHeader(
                AttendanceDayHeaderItem(
                    it
                )
            )
            addAll(it.lekcje.map {
                val godz = godziny[it.index-1].split(" ")
                val godzinyFormatted = "${godz[0]} - ${godz[1]}"
                AttendanceLessonItem(
                    it,
                    godzinyFormatted
                )
            })
        }
    }
}