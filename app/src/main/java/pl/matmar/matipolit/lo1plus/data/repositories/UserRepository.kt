package pl.matmar.matipolit.lo1plus.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.matmar.matipolit.lo1plus.data.network.AuthResponse
import pl.matmar.matipolit.lo1plus.data.network.LoginApi
import retrofit2.Response

class UserRepository {
    suspend fun userLogin(email: String, password: String) : Response<AuthResponse> {
        lateinit var loginResponse: Response<AuthResponse>
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
            loginResponse = LoginApi.login.userLogin(email, password)
        }
        return loginResponse
    }
}