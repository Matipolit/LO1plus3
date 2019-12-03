package pl.matmar.matipolit.lo1plus.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

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

/*@Entity
data class Card(
    @PrimaryKey(autoGenerate = false)
    var name: String? = null,
    var content: String? = null
)*/
