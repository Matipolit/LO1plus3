package pl.matmar.matipolit.lo1plus.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import pl.matmar.matipolit.lo1plus.data.network.AuthResponse
import pl.matmar.matipolit.lo1plus.data.network.LoginApi
import retrofit2.Response
import retrofit2.await
import java.lang.Exception

class UserRepository {
    suspend fun userLogin(email: String, password: String) : Response<AuthResponse> {
        val loginResponse = Response<AuthResponse>()
        withContext(Dispatchers.Main){
            try{
                val response: ResponseBody = LoginApi.retrofitService.userLogin(email, password).await()
                loginResponse.value = response.string()
            }catch (e: Exception) {
                e.printStackTrace()
                loginResponse.value = e.message
            }
        }
        return loginResponse
    }
}