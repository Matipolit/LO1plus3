package pl.matmar.matipolit.lo1plus.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.matmar.matipolit.lo1plus.domain.HomeCard
import pl.matmar.matipolit.lo1plus.utils.toCardColorInt
import pl.matmar.matipolit.lo1plus.utils.toCardIcon
import pl.matmar.matipolit.lo1plus.utils.toCardTitle
import pl.matmar.matipolit.lo1plus.utils.toCardType

const val CURRENT_USERDB_ID = 0
const val CURRENT_GODZINY_ID = 0

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
    var jutroName: String,
    var jutroData: String,
    var dzwonekDelay: Int?,
    var data: String?
){
    @PrimaryKey(autoGenerate = false)
    var databaseId: Int = CURRENT_GODZINY_ID
}

//TODO add cardlist