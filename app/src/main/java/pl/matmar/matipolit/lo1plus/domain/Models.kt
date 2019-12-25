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
}

data class Grades(
    val oceny: List<Subject>,
    val semestr: Int,
    val semestr1ID: Int,
    val klasa: String?,
    val date: Date?
){
    val average = oceny.map { it->it.sredniaFloat }.average()
}