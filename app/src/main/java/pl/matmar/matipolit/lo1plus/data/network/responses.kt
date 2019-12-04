package pl.matmar.matipolit.lo1plus.data.network

import pl.matmar.matipolit.lo1plus.utils.GodzinyJSON


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
    val correct: String?,
    val planLekcji: String?,
    val ostatnieOceny: String?,
    val wiadomosci: String?,
    val obiady: String?,
    val ogloszenia: String?,
    val terminySprawdzianow: String?,
    val godziny: GodzinyJSON
)

