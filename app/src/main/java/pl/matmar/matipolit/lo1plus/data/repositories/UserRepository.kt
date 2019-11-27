package pl.matmar.matipolit.lo1plus.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.matmar.matipolit.lo1plus.data.network.AuthResponse
import pl.matmar.matipolit.lo1plus.data.network.LoginApi
import pl.matmar.matipolit.lo1plus.data.network.SafeApiRequest
import pl.matmar.matipolit.lo1plus.utils.ApiException
import retrofit2.Response

class UserRepository : SafeApiRequest(){
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
            return loginResponse
        }else{
            loginResponse.info?.let {
                throw ApiException(loginResponse.info!!)
            }
            throw ApiException("Wrong email or password")
        }
    }
}