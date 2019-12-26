package pl.matmar.matipolit.lo1plus.domain

import pl.matmar.matipolit.lo1plus.utils.toCardType
import pl.matmar.matipolit.lo1plus.utils.toFormattedSpannable
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

data class Grade(
    val kod: String,
    val opis: String,
    val waga: String,
    val data: String,
    val nauczyciel: String,
    val ocena: String,
    val komentarz: String?
){
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
    var sredniaText : String? = null
    var sredniaVal : String? = null
    init {
        if(ocena_roczna != "-"){
            sredniaText = "Ocena roczna"
            sredniaVal = ocena_roczna
        }else if(przewidywana_roczna != "-"){
            sredniaText = "Przewidywana ocena roczna"
            sredniaVal = przewidywana_roczna
        }else if(ocena_śródroczna != "-"){
            sredniaText = "Ocena śródroczna"
            sredniaVal = ocena_śródroczna
        }else if(przewidywana_śródroczna != "-"){
            sredniaText = "Przewidywana ocena śródroczna"
            sredniaVal = przewidywana_śródroczna
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
                sumaRocznych += subject.sredniaVal?.toFloat() ?: 0F
                iloscRocznych += 1
                if(subject.ocena_roczna != "-"){
                    areRoczne = true
                }else{
                    arePrzewidywaneRoczne = true
                }
            }
            if(subject.ocena_śródroczna != "-" || subject.przewidywana_śródroczna != "-"){
                sumaSrodrocznych += subject.sredniaVal?.toFloat() ?: 0F
                iloscSrodrocznych += 1
                if(subject.ocena_śródroczna != "-"){
                    areSrodroczne = true
                }else{
                    arePrzewidywaneRoczne = true
                }
            }
        }
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