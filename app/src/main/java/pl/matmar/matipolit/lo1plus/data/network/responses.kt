package pl.matmar.matipolit.lo1plus.data.network

import com.google.gson.Gson
import org.json.JSONObject
import pl.matmar.matipolit.lo1plus.data.database.DatabaseAttendance
import pl.matmar.matipolit.lo1plus.data.database.DatabaseGrades
import pl.matmar.matipolit.lo1plus.data.database.DatabasePlan
import pl.matmar.matipolit.lo1plus.domain.Attendance
import pl.matmar.matipolit.lo1plus.domain.Plan
import pl.matmar.matipolit.lo1plus.domain.PlanWrapper
import pl.matmar.matipolit.lo1plus.domain.Subject
import java.util.*


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

data class PlanResponse(
    val correct: String,
    val info: String?,
    val planLekcji: Plan,
    val klasa: String?

)

data class AttResponse(
    val correct: String,
    val info: String?,
    val frekwencja: Attendance,
    val klasa: String?
)

data class PlansResponse(
    val legend: PlansLegend,
    val plany: JSONObject
)

fun PlanResponse.asDomainModel():PlanWrapper = PlanWrapper(this.planLekcji, this.klasa)

fun PlanResponse.asDatabaseModel(cal: Calendar): DatabasePlan = DatabasePlan(this.planLekcji, this.klasa, cal)

fun AttResponse.asDatabaseModel(cal: Calendar): DatabaseAttendance = DatabaseAttendance(this.frekwencja, this.klasa, cal)

fun GradesResponse.asDatabaseModel() : DatabaseGrades =
    DatabaseGrades(Gson().toJson(this.oceny), this.semestr, this.semestr1ID, this.klasa, this.date)

