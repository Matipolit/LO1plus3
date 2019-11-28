package pl.matmar.matipolit.lo1plus.data.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.matmar.matipolit.lo1plus.data.database.User
import pl.matmar.matipolit.lo1plus.data.database.UserDatabase
import pl.matmar.matipolit.lo1plus.data.network.AuthResponse
import pl.matmar.matipolit.lo1plus.data.network.LoginApi
import pl.matmar.matipolit.lo1plus.data.network.SafeApiRequest
import pl.matmar.matipolit.lo1plus.utils.ApiException
import retrofit2.Response

class UserRepository(private val database: UserDatabase) : SafeApiRequest(){
    suspend fun userLogin(email: String, password: String) : AuthResponse {
        lateinit var loginResponse: AuthResponse
        /*withContext(Dispatchers.Main){
            try{
                val response: ResponseBody = LoginApi.retrofitService.userLogin(email, password).await()
                loginResponse.value = response.string()
            }catch (e: Exception) {
                e.printStackTrace()
                loginResponse.value = e.message
            }
        }
         */
        withContext(Dispatchers.IO){
            //loginResponse = LoginApi.login.userLogin(email, password)
            loginResponse = apiRequest{LoginApi.login.userLogin(email, password)}
        }
        if(loginResponse.correct.equals("true")){
            val user = User(loginResponse.userID, loginResponse.admin, loginResponse.obiadyAdmin, email)
            withContext(Dispatchers.IO){
                saveUser(user)
            }
            return loginResponse
        }else{
            loginResponse.info?.let {
                throw ApiException(loginResponse.info!!)
            }
            if(loginResponse.correct.equals("notLO1")){
                throw ApiException("Nie jesteś uczniem LO1")
            }
            throw ApiException("Zły email lub hasło")
        }
    }

    val user: LiveData<User> = database.userDao.getUser()

    private suspend fun saveUser(user: User) = database.userDao.upsert(user)
}