package pl.matmar.matipolit.lo1plus.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import pl.matmar.matipolit.lo1plus.domain.Grades
import pl.matmar.matipolit.lo1plus.domain.HomeCard
import pl.matmar.matipolit.lo1plus.domain.Subject
import pl.matmar.matipolit.lo1plus.utils.*
import java.lang.reflect.Type

const val CURRENT_USERDB_ID = 0
const val CURRENT_GODZINY_ID = 1

@Entity
data class User(
    var userID: String? = null,
    var admin: Boolean? = null,
    var obiadyAdmin: Boolean? = null,
    var email: String?
){
    @PrimaryKey(autoGenerate = false)
    var databaseId: Int = CURRENT_USERDB_ID
}

/*@Entity
data class Home(
    var cards: List<Pair<Int, String>>? = null,
    var godziny: Godziny? = null,
    var card_list : List<Int> = DEFAULT_CARD_LIST.map { it.toCardType() }
){
    @PrimaryKey(autoGenerate = false)
    var databaseId: Int = CURRENT_HOMEDB_ID
}*/

//Home

@Entity
data class DatabaseCard(
    @PrimaryKey
    var name: String,
    var content: String
){
    var id = name.toCardType()
}

fun List<DatabaseCard>.asDomainModel(): List<HomeCard>{
    return map{
            HomeCard(
                title = it.name.toCardTitle(),
                content = it.content,
                color = it.name.toCardColorInt(),
                icon = it.name.toCardIcon()

            )
        }
}

@Entity
data class DatabaseGodziny(
    var godziny: String,
    var jutro: String,
    val jutroTime: String,
    var jutroName: String,
    var jutroData: String,
    var dzwonekDelay: Int?,
    var data: String?
){
    @PrimaryKey(autoGenerate = false)
    var databaseId: Int = CURRENT_GODZINY_ID
}

fun DatabaseGodziny.asGodzinyJSON(): GodzinyJSON?{
    return(GodzinyJSON(JSONObject(godziny),jutro, jutroTime, jutroName, jutroData,
        dzwonekDelay, data ))
}

fun JSONObject.asDatabaseGodziny() : DatabaseGodziny{
    return DatabaseGodziny(
        this.getString("godziny"),
        this.optString("jutro"),
        this.getString("jutroTime"),
        this.getString("jutroName"),
        this.optString("jutroData"),
        this.optString("dzwonekDelay").toInt(),
        this.optString("data")
    )
}

//Grades

@Entity
data class DatabaseGrades(
    var oceny: String,
    var semestr: Int,
    var semestr1ID: Int,
    var klasa: String,
    var date: String
)

fun DatabaseGrades.asDomainModel() : Grades{
    val listType: Type = object : TypeToken<List<Subject?>?>() {}.getType()
    val subjects : List<Subject> = Gson().fromJson(this.oceny, listType)
    return Grades(subjects, this.semestr, this.semestr1ID, this.klasa, this.date.toDate()!!)
}