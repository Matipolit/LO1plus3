package pl.matmar.matipolit.lo1plus.data.network

data class AuthResponse(
    val correct : String,
    //val home: Home?,
    val admin: Boolean?,
    val obiadyAdmin: Boolean?,
    val info: String?,
    val newUser: Boolean?,
    val userID: String?
)