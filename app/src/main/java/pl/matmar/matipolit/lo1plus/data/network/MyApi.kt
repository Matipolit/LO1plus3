package pl.matmar.matipolit.lo1plus.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.ResponseBody
import pl.matmar.matipolit.lo1plus.utils.BASE_URL
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

interface LoginApiService {
    @POST("login")
    suspend fun userLogin(
        @Query("email")email: String,
        @Query("haslo")password: String) : Response<AuthResponse>
}

object LoginApi {
    /*val retrofitService: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }*/
    val login = retrofit.create(LoginApiService::class.java)
}