package pl.matmar.matipolit.lo1plus.data.network

import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend() -> Response<T>) :T{
        val response = call.invoke()

        if(response.isSuccessful){
            return response.body()!!
        }else{
            val error = response.errorBody()?.string()
        }
    }
}