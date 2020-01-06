package pl.matmar.matipolit.lo1plus.data.network

import org.json.JSONException
import org.json.JSONObject
import pl.matmar.matipolit.lo1plus.utils.ApiException
import retrofit2.Response
import timber.log.Timber

abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend() -> Response<T>) :T{
        val response = call.invoke()

        if(response.isSuccessful){
            Timber.d("response was successful")
            Timber.d(response.body().toString())
            return response.body()!!
        }else{
            Timber.e("response was unsuccessful")
            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error?.let {
                Timber.e("Error is not null")
                try{
                    message.append(JSONObject(it).getString("info"))
                }catch (e: JSONException){
                    Timber.d("No info field")
                    e.printStackTrace()
                }
                message.append("\n")
            }
            message.append("Kod błędu: ${response.code()}")
            Timber.d("Appended message")
            throw ApiException(message.toString())
        }
    }
}