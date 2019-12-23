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
    val waga: Int,
    val data: String,
    val nauczyciel: String,
    val ocena: Int,
    val komentarz: String?
)

data class Subject(
    val name: String,
    val GradeList: List<Grade>,
    val srednia: String,
    val przewidywana_śródroczna: String,
    val ocena_śródroczna: String,
    val przewidywana_roczna: String,
    val ocena_roczna: String
)

data class Grades(
    val oceny: List<Subject>,
    val semestr: Int,
    val semestr1ID: Int,
    val klasa: String,
    val date: Date
)