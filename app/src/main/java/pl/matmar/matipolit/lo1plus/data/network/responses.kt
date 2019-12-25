package pl.matmar.matipolit.lo1plus.data.network

import com.google.gson.Gson
import pl.matmar.matipolit.lo1plus.data.database.DatabaseGrades
import pl.matmar.matipolit.lo1plus.domain.Subject


data class AuthResponse(
    val correct : String,
    val home: HomeResponse?,
    val admin: Boolean?,
    val obiadyAdmin: Boolean?,
    val info: String?,
    val newUser: Boolean?,
    val userID: String?
)

data class HomeResponse(
    val correct: String,
    val info: String?,
    val planLekcji: String?,
    val ostatnieOceny: String?,
    val wiadomosci: String?,
    val obiady: String?,
    val ogloszenia: String?,
    val terminySprawdzianow: String?,
    val najblizszeDniWolne: String?,
    val godziny: String?
)

data class GradesResponse(
    val correct: String,
    val info: String?,
    var oceny: Array<Subject>,
    var semestr: Int,
    var semestr1ID: Int,
    var klasa: String?,
    var date: String?
)

fun GradesResponse.asDatabaseModel() : DatabaseGrades =
    DatabaseGrades(Gson().toJson(this.oceny), this.semestr, this.semestr1ID, this.klasa, this.date)

