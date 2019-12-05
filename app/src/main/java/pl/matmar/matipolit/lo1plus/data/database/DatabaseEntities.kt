package pl.matmar.matipolit.lo1plus.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.matmar.matipolit.lo1plus.utils.Godziny
import pl.matmar.matipolit.lo1plus.utils.STANDARD_CARD_LIST
import pl.matmar.matipolit.lo1plus.utils.toCardType

const val CURRENT_USERDB_ID = 0
const val CURRENT_HOMEDB_ID = 1

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
data class Home(
    var cards: Pair<Int, String>? = null,
    var godziny: Godziny? = null,
    var card_list : List<Int> = STANDARD_CARD_LIST.map { it.toCardType() }
){
    @PrimaryKey(autoGenerate = false)
    var databaseId: Int = CURRENT_HOMEDB_ID
}

//TODO add cardlist