package pl.matmar.matipolit.lo1plus.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.matmar.matipolit.lo1plus.utils.Godzina
import java.util.*

const val CURRENT_USERDB_ID = 0

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

@Entity
data class Godziny(
    @PrimaryKey(autoGenerate = false)
    var godziny: List<Godzina>?,
    var jutro: Date?,
    var jutroFirstLesson: String?,
    var dzwonekDelay: Int?,
    var date: Date
)
