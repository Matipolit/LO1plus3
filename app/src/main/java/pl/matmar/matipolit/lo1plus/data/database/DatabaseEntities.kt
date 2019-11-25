package pl.matmar.matipolit.lo1plus.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USERDB_ID = 0

@Entity
data class User(
    var userID: Int? = null,
    var admin: Boolean? = null,
    var obiadyAdmin: Boolean? = null
){
    @PrimaryKey(autoGenerate = false)
    var databaseId: Int = CURRENT_USERDB_ID
}