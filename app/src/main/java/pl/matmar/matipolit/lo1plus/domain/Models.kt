package pl.matmar.matipolit.lo1plus.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.matmar.matipolit.lo1plus.utils.toCardType
import pl.matmar.matipolit.lo1plus.utils.toFormattedSpannable
import java.text.SimpleDateFormat
import java.util.*

data class HomeCard(
    val title: String,
    val content: String,
    val color: Int,
    val icon: Int
){
    val formattedContent = content.toFormattedSpannable()
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
    val sredniaFloat = srednia.toFloat()
    val sredniaText : String? = when {
        !ocena_roczna.equals("-") -> "Ocena roczna"
        !przewidywana_roczna.equals("-") -> "Ocena przewidywana roczna"
        !ocena_śródroczna.equals("-") -> "Ocena śródroczna"
        !przewidywana_śródroczna.equals("-") -> "Ocena przewidywana śródroczna"
        else -> null
    }
    val sredniaVal : String? = when {
        !ocena_roczna.equals("-") -> ocena_roczna
        !przewidywana_roczna.equals("-") -> przewidywana_roczna
        !ocena_śródroczna.equals("-") -> ocena_śródroczna
        !przewidywana_śródroczna.equals("-") -> przewidywana_śródroczna
        else -> null
    }
}

data class Grades(
    val oceny: List<Subject>,
    val semestr: Int,
    val semestr1ID: Int,
    val klasa: String?,
    val date: Date?
){
    val subjectAverage = oceny.map { it.sredniaFloat }.average()
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
                if(subject.ocena_roczna != "-"){
                    areRoczne = true
                    sumaRocznych += subject.ocena_roczna.toFloat()
                }else{
                    arePrzewidywaneRoczne = true
                    sumaRocznych += subject.przewidywana_roczna.toFloat()
                }
            }
            if(subject.ocena_śródroczna != "-" || subject.przewidywana_śródroczna != "-"){
                iloscSrodrocznych += 1
                if(subject.ocena_śródroczna != "-"){
                    areSrodroczne = true
                    sumaSrodrocznych += subject.ocena_śródroczna.toFloat()
                }else{
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

data class Plan(
    val planLekcji: PlanLekcji,
    val klasa: String?
)

data class PlanLekcji(
    val godziny: List<String>,
    val tydzien: List<WeekDay>
){
    val godzinySplit = godziny.map {
        it.split(" ")
    }
    var begDate: Date? = null
    var endDate: Date? = null
    init {
        if(godziny.size >= 2){
            begDate = SimpleDateFormat("dd.MM.yyyy", Locale.US).parse(tydzien.first().date)
            endDate = SimpleDateFormat("dd.MM.yyyy", Locale.US).parse(tydzien.last().date)
        }
    }

}

data class WeekDay(
    val name: String,
    val date: String,
    val lekcje: List<Lekcja>
)

data class Lekcja(
    val index: Int,
    val data: String
)